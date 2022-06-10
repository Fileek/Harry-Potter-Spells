package com.epam.harrypotterspells.feature.main

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.feature.main.MainAction.SwitchViewToLocalAction
import com.epam.harrypotterspells.feature.main.MainAction.SwitchViewToRemoteAction
import com.epam.harrypotterspells.feature.main.MainIntent.SwitchToLocalIntent
import com.epam.harrypotterspells.feature.main.MainIntent.SwitchToRemoteIntent
import com.epam.harrypotterspells.feature.main.MainResult.SwitchToLocalResult
import com.epam.harrypotterspells.feature.main.MainResult.SwitchToRemoteResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject

class MainViewModel : ViewModel(), MVIViewModel<MainIntent, MainViewState> {

    private val intentsSubject = BehaviorSubject.create<MainIntent>()
    private val initialState = MainViewState()
    private val statesObservable = compose()

    override fun processIntents(observable: Observable<MainIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun getStates(): Observable<MainViewState> = statesObservable

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
        is SwitchToRemoteIntent -> SwitchViewToRemoteAction
        is SwitchToLocalIntent -> SwitchViewToLocalAction
    }

    private fun performActions() = ObservableTransformer<MainAction, MainResult> { actions ->
        Observable.merge(
            actions.ofType(SwitchViewToRemoteAction::class.java).map {
                SwitchToRemoteResult
            },
            actions.ofType(SwitchViewToLocalAction::class.java).map {
                SwitchToLocalResult
            }
        )
    }

    private companion object {
        private const val VIEW_STATE_BUFFER_SIZE = 1

        /**
         * Returns new [MainViewState] by applying given [MainResult] on given [MainViewState].
         */
        private val reducer =
            BiFunction<MainViewState, MainResult, MainViewState> { state, result ->
                when (result) {
                    is SwitchToRemoteResult -> state.copy(isRemote = true)
                    is SwitchToLocalResult -> state.copy(isRemote = false)
                }
            }
    }
}