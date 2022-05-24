package com.epam.harrypotterspells.feature.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadFilteredAction
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadFilteredAction.LoadLocalAction
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadFilteredAction.LoadRemoteAction
import com.epam.harrypotterspells.feature.spells.SpellsIntent.LoadIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.LoadLocalIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.LoadRemoteIntent
import com.epam.harrypotterspells.feature.spells.SpellsIntent.SearchByQueryIntent
import com.epam.harrypotterspells.feature.spells.SpellsResult.Error
import com.epam.harrypotterspells.feature.spells.SpellsResult.Loading
import com.epam.harrypotterspells.feature.spells.SpellsResult.Success
import com.epam.harrypotterspells.mvibase.MVIViewModel
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val loadRemoteFilteredSpellsUseCase: UseCase<LoadRemoteAction, SpellsResult>,
    private val loadLocalFilteredSpellsUseCase: UseCase<LoadLocalAction, SpellsResult>
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private var isRemote = true
    private var searchQuery = ""
    private val intentsSubject = BehaviorSubject.create<SpellsIntent>()
    private val initialState = SpellsViewState()
    private val statesObservable: Observable<SpellsViewState> = compose()

    /**
     * Composes [SpellsViewState] based on received intents in [intentsSubject]
     */
    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .map(this::getActionFromIntent)
            .compose(performActions())
            .scan(initialState, reducer)
            .observeOn(schedulerProvider.getUIScheduler())
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect(NUMBER_OF_OBSERVERS)
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: SpellsIntent) = when (intent) {
        is LoadIntent -> getLoadFilteredAction()
        is LoadRemoteIntent -> setIsRemoteAndGetLoadRemoteAction()
        is LoadLocalIntent -> setIsRemoteAndGetLoadLocalAction()
        is SearchByQueryIntent -> setSearchQueryAndGetAction(intent)
    }

    private fun getLoadFilteredAction(): LoadFilteredAction {
        return if (isRemote) LoadRemoteAction(searchQuery)
        else LoadLocalAction(searchQuery)
    }

    private fun setIsRemoteAndGetLoadRemoteAction(): LoadRemoteAction {
        isRemote = true
        return LoadRemoteAction(searchQuery)
    }

    private fun setIsRemoteAndGetLoadLocalAction(): LoadLocalAction {
        isRemote = false
        return LoadLocalAction(searchQuery)
    }

    private fun setSearchQueryAndGetAction(intent: SearchByQueryIntent): SpellsAction {
        searchQuery = intent.query
        return getLoadFilteredAction()
    }

    private fun performActions() = ObservableTransformer<SpellsAction, SpellsResult> { actions ->
        Observable.merge(
            actions.ofType(LoadRemoteAction::class.java).compose(
                loadRemoteFilteredSpellsUseCase.performAction()
            ),
            actions.ofType(LoadLocalAction::class.java).compose(
                loadLocalFilteredSpellsUseCase.performAction()
            ),
        )
    }

    override fun processIntents(observable: Observable<SpellsIntent>) {
        observable.subscribe {
            intentsSubject.onNext(it)
        }
    }

    override fun getStates(): Observable<SpellsViewState> = statesObservable

    private companion object {
        private const val NUMBER_OF_OBSERVERS = 0
        private const val VIEW_STATE_BUFFER_SIZE = 1

        /**
         * Returns new [SpellsViewState] by applying given [SpellsResult] on given [SpellsViewState].
         */
        private val reducer =
            BiFunction<SpellsViewState, SpellsResult, SpellsViewState> { state, result ->
                when (result) {
                    is Loading -> state.copy(isLoading = true)
                    is Success -> state.copy(isLoading = false, data = result.data)
                    is Error -> state.copy(isLoading = false, error = result.error)
                }
            }
    }
}