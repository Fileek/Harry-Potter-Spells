package com.epam.harrypotterspells.feature.main

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.feature.main.MainAction.SearchByQueryAction
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction
import com.epam.harrypotterspells.feature.main.MainIntent.SearchByQueryIntent
import com.epam.harrypotterspells.feature.main.MainIntent.SwitchSourceIntent
import com.epam.harrypotterspells.feature.main.MainResult.SearchByQueryResult
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUseCase: UseCase<SearchByQueryAction, SearchByQueryResult>,
    private val switchSourceUseCase: UseCase<SwitchSourceAction, SwitchSourceResult>,
) : ViewModel(), MVIViewModel<MainIntent, MainViewState> {

    private val intentsSubject = BehaviorSubject.create<MainIntent>()
    private val initialState = MainViewState()
    private val statesObservable = compose()

    /**
     * Composes [MainViewState] based on received intents in [intentsSubject]
     */
    private fun compose(): Observable<MainViewState> {
        return intentsSubject
            .map(this::getActionFromIntent)
            .compose(performActions())
            .scan(initialState, reducer)
            .replay(VIEW_STATE_BUFFER_SIZE)
            .autoConnect()
            .distinctUntilChanged()
    }

    private fun getActionFromIntent(intent: MainIntent) = when (intent) {
        is SwitchSourceIntent -> getActionFromSwitchSourceIntent(intent)
        is SearchByQueryIntent -> SearchByQueryAction(intent.query)
    }

    private fun getActionFromSwitchSourceIntent(intent: SwitchSourceIntent) = when (intent) {
        is SwitchSourceIntent.ToRemoteIntent -> SwitchSourceAction.ToRemoteAction
        is SwitchSourceIntent.ToLocalIntent -> SwitchSourceAction.ToLocalAction
    }

    private fun performActions() = ObservableTransformer<MainAction, MainResult> { actions ->
        Observable.merge(
            actions.ofType(SearchByQueryAction::class.java).compose(
                searchUseCase.performAction()
            ),
            actions.ofType(SwitchSourceAction::class.java).compose(
                switchSourceUseCase.performAction()
            )
        )
    }

    override fun processIntents(observable: Observable<MainIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun getStates(): Observable<MainViewState> = statesObservable

    private companion object {
        private const val VIEW_STATE_BUFFER_SIZE = 1

        /**
         * Returns new [MainViewState] by applying given [MainResult] on given [MainViewState].
         */
        private val reducer =
            BiFunction<MainViewState, MainResult, MainViewState> { state, result ->
                when (result) {
                    is SearchByQueryResult -> state.copy()
                    is SwitchSourceResult -> {
                        when (result) {
                            is SwitchSourceResult.ToRemoteResult -> state.copy(isRemote = true)
                            is SwitchSourceResult.ToLocalResult -> state.copy(isRemote = false)
                        }
                    }
                }
            }
    }
}