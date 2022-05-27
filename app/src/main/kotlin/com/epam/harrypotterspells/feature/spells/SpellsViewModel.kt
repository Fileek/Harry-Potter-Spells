package com.epam.harrypotterspells.feature.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.GetLocalSpellsUseCase
import com.epam.harrypotterspells.domain.GetRemoteSpellsUseCase
import com.epam.harrypotterspells.domain.SaveLocalSpellUseCase
import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.spells.SpellsAction.GetCacheAction
import com.epam.harrypotterspells.feature.spells.SpellsAction.GetLocalAction
import com.epam.harrypotterspells.feature.spells.SpellsAction.GetRemoteAction
import com.epam.harrypotterspells.feature.spells.SpellsAction.SaveSpellAction
import com.epam.harrypotterspells.feature.spells.SpellsIntent.GetLocalIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.GetRemoteIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.InitialIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.SaveSpellIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.SearchByQueryIntent
import com.epam.harrypotterspells.feature.spells.SpellsResult.LocalResult
import com.epam.harrypotterspells.feature.spells.SpellsResult.RemoteResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import com.epam.harrypotterspells.util.span.SubstringHighlighter
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.Collections
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val getRemoteSpellsUseCase: GetRemoteSpellsUseCase,
    private val getLocalSpellsUseCase: GetLocalSpellsUseCase,
    private val saveLocalSpellUseCase: SaveLocalSpellUseCase
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private var isRemote = true
    private var searchQuery = ""
    private var cachedSpells = emptyList<Spell>()

    private val intentsSubject = BehaviorSubject.create<SpellsIntent>()
    private val initialState = SpellsViewState()
    private val statesObservable: Observable<SpellsViewState> = compose()

    override fun processIntents(observable: Observable<SpellsIntent>) {
        observable.subscribe {
            intentsSubject.onNext(it)
        }
    }

    override fun getStates(): Observable<SpellsViewState> = statesObservable

    /**
     * Composes [SpellsViewState] based on received intents in [intentsSubject]
     */
    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .map(this::getActionFromIntent)
            .compose(performActions())
            .scan(initialState, reduce())
            .observeOn(schedulerProvider.getUIScheduler())
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect(NUMBER_OF_OBSERVERS)
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: SpellsIntent) = when (intent) {
        is InitialIntent -> getLoadAction()
        is GetRemoteIntent -> setIsRemoteAndGetLoadAction()
        is GetLocalIntent -> setIsNotRemoteAndGetLoadAction()
        is SearchByQueryIntent -> setSearchQueryAndGetLoadAction(intent)
        is SaveSpellIntent -> getSaveAction(intent)
    }

    private fun getLoadAction(): SpellsAction {
        return if (isRemote) getLoadRemoteOrCacheAction()
        else GetLocalAction
    }

    private fun getLoadRemoteOrCacheAction(): SpellsAction {
        return if (cachedSpells.isEmpty()) GetRemoteAction
        else GetCacheAction
    }

    private fun setIsRemoteAndGetLoadAction(): SpellsAction {
        isRemote = true
        return getLoadAction()
    }

    private fun setIsNotRemoteAndGetLoadAction(): SpellsAction {
        isRemote = false
        return getLoadAction()
    }

    private fun setSearchQueryAndGetLoadAction(intent: SearchByQueryIntent): SpellsAction {
        searchQuery = intent.query
        return getLoadAction()
    }

    private fun getSaveAction(intent: SaveSpellIntent) = SaveSpellAction(intent.spell)

    private fun performActions() = ObservableTransformer<SpellsAction, SpellsResult> { actions ->
        Observable.merge(
            actions.ofType(GetRemoteAction::class.java).compose(
                getRemoteSpellsUseCase.performAction()
            ),
            actions.ofType(GetCacheAction::class.java).compose(
                performLoadCacheAction()
            ),
            actions.ofType(GetLocalAction::class.java).compose(
                getLocalSpellsUseCase.performAction()
            ),
            actions.ofType(SaveSpellAction::class.java).compose(
                performSaveSpellAction()
            )
        )
    }

    private fun performLoadCacheAction() = ObservableTransformer<GetCacheAction, SpellsResult> {
        it.map { RemoteResult.Success(cachedSpells) }
    }

    private fun performSaveSpellAction() = ObservableTransformer<SaveSpellAction, SpellsResult> {
        it.flatMap { action ->
            if (isRemote) saveCachedSpell(action.spell)
            else saveLocalSpellUseCase.performAction(action.spell)
        }
    }

    private fun saveCachedSpell(newSpell: Spell): Observable<RemoteResult> {
        val oldSpell = cachedSpells.find { oldSpell -> oldSpell.id == newSpell.id }
        Collections.replaceAll(cachedSpells, oldSpell, newSpell)
        return Observable.just(RemoteResult.Success(cachedSpells))
    }

    /**
     * Returns new [SpellsViewState] by applying given [SpellsResult] on given [SpellsViewState].
     */
    private fun reduce() =
        BiFunction<SpellsViewState, SpellsResult, SpellsViewState> { state, result ->
            when (result) {
                is RemoteResult -> reduceRemoteResult(state, result)
                is LocalResult -> reduceLocalResult(state, result)
            }
        }

    private fun reduceRemoteResult(state: SpellsViewState, result: RemoteResult): SpellsViewState {
        return when (result) {
            is RemoteResult.Loading -> {
                state.copy(isLoading = true)
            }
            is RemoteResult.Success -> {
                cachedSpells = result.data
                state.copy(
                    isLoading = false,
                    data = filterAndHighlightBySearchQuery(result.data)
                )
            }
            is RemoteResult.Error -> {
                state.copy(isLoading = false, error = result.error)
            }
        }
    }

    private fun reduceLocalResult(state: SpellsViewState, result: LocalResult): SpellsViewState {
        return state.copy(isLoading = false, data = filterAndHighlightBySearchQuery(result.data))
    }

    private fun filterAndHighlightBySearchQuery(spells: List<Spell>): List<SpannedSpell> {
        val filteredSpells = filterSpellsBySearchQuery(spells)
        return highlightSearchQuery(filteredSpells)
    }

    private fun filterSpellsBySearchQuery(spells: List<Spell>): List<Spell> {
        return spells.filter { spell ->
            with(spell) {
                name.contains(searchQuery, IGNORE_CASE) ||
                incantation.contains(searchQuery, IGNORE_CASE) ||
                effect.contains(searchQuery, IGNORE_CASE) ||
                type.contains(searchQuery, IGNORE_CASE)
            }
        }
    }

    private fun highlightSearchQuery(spells: List<Spell>): List<SpannedSpell> {
        val substringHighlighter = SubstringHighlighter(searchQuery, IGNORE_CASE)
        return spells.map { spell ->
            with(spell) {
                SpannedSpell(
                    id = id,
                    name = substringHighlighter.invoke(name),
                    incantation = substringHighlighter.invoke(incantation),
                    type = substringHighlighter.invoke(type),
                    effect = substringHighlighter.invoke(effect),
                    light = light,
                    creator = creator,
                    canBeVerbal = canBeVerbal
                )
            }
        }
    }

    private companion object {
        private const val IGNORE_CASE = true
        private const val NUMBER_OF_OBSERVERS = 0
        private const val VIEW_STATE_BUFFER_SIZE = 1
    }
}