package com.epam.harrypotterspells.feature.spells

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LoadResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val loadUseCase: UseCase<LoadAction, LoadResult>
) : ViewModel(), MVIViewModel<SpellsIntent, SpellsViewState> {

    private val intentsSubject = BehaviorSubject.create<SpellsIntent>()
    private val initialState = SpellsViewState()
    private val statesObservable: Observable<SpellsViewState> = compose()

    /**
     * Composes [SpellsViewState] based on received intents in [intentsSubject]
     */
    private fun compose(): Observable<SpellsViewState> {
        return intentsSubject
            .map(this::getActionFromIntent)
            .compose(loadUseCase.performAction())
            .scan(initialState, reducer)
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect(NUMBER_OF_OBSERVERS)
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: SpellsIntent) = when (intent) {
        is SpellsIntent.LoadIntent -> LoadAction
    }

    override fun processIntents(observable: Observable<SpellsIntent>) {
        observable.subscribe(intentsSubject)
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
                    is LoadResult.Success -> state.copy(isLoading = false, data = result.data)
                    is LoadResult.Error -> state.copy(isLoading = false, error = result.error)
                    is LoadResult.Loading -> state.copy(isLoading = true)
                }
            }
    }
}