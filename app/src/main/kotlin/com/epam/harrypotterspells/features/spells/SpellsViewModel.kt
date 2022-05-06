package com.epam.harrypotterspells.features.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.features.spells.SpellsResult.LoadSpellsResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val loadSpellsUseCase: LoadSpellsUseCase
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private val intentsSubject = BehaviorSubject.create<SpellsIntent>()
    private val statesObservable: Observable<SpellsViewState> = compose()

    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .observeOn(Schedulers.computation())
            .map(this::actionFromIntent)
            .compose(loadSpellsUseCase.performAction())
            .scan(SpellsViewState.Idle, reducer)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
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