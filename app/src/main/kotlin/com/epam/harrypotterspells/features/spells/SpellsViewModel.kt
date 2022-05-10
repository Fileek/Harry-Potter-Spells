package com.epam.harrypotterspells.features.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.features.spells.SpellsAction.LoadSpellsAction
import com.epam.harrypotterspells.features.spells.SpellsResult.LoadSpellsResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import com.epam.harrypotterspells.utils.schedulers.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val loadSpellsUseCase: UseCase<LoadSpellsAction, LoadSpellsResult>
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private val intentsSubject = BehaviorSubject.create<SpellsIntent>()
    private val statesObservable: Observable<SpellsViewState> = compose()

    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .observeOn(schedulerProvider.computation())
            .map(this::getActionFromIntent)
            .compose(loadSpellsUseCase.performAction())
            .scan(SpellsViewState.Idle, reducer)
            .distinctUntilChanged()
            .observeOn(schedulerProvider.ui())
            .replay()
            .autoConnect()
    }

    private fun getActionFromIntent(intent: SpellsIntent) = when (intent) {
        is SpellsIntent.LoadSpellsIntent -> LoadSpellsAction
    }

    override fun processIntents(observable: Observable<SpellsIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun getStates(): Observable<SpellsViewState> = statesObservable

    private companion object {

        private val reducer = BiFunction { _: SpellsViewState, result: SpellsResult ->
            when (result) {
                is LoadSpellsResult.Success -> SpellsViewState.Success(result.data)
                is LoadSpellsResult.Error -> SpellsViewState.Error(result.error)
                is LoadSpellsResult.Loading -> SpellsViewState.Loading
            }
        }
    }
}