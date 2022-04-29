package com.epam.harrypotterspells.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.mvibase.MVIProcessorHolder
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.epam.harrypotterspells.spells.SpellsResult.LoadSpellsResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val processorHolder: MVIProcessorHolder<SpellsAction, SpellsResult>
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private val intentsSubject = PublishSubject.create<SpellsIntent>()
    private val statesObservable: Observable<SpellsViewState> = compose()

    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .map(this::actionFromIntent)
            .compose(processorHolder.actionProcessor)
            .scan(SpellsViewState.Idle, reducer)
            .replay()
            .autoConnect()
    }

    private fun actionFromIntent(intent: SpellsIntent): SpellsAction {
        return when (intent) {
            is SpellsIntent.LoadSpellsIntent -> SpellsAction.LoadSpellsAction
        }
    }

    override fun processIntents(observable: Observable<SpellsIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun states(): Observable<SpellsViewState> = statesObservable

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