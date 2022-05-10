package com.epam.harrypotterspells.main

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.main.MainAction.SwitchToLocalAction
import com.epam.harrypotterspells.main.MainAction.SwitchToRemoteAction
import com.epam.harrypotterspells.main.MainIntent.SwitchToLocalIntent
import com.epam.harrypotterspells.main.MainIntent.SwitchToRemoteIntent
import com.epam.harrypotterspells.main.MainResult.SwitchToLocalResult
import com.epam.harrypotterspells.main.MainResult.SwitchToRemoteResult
import com.epam.harrypotterspells.mvibase.MVIViewModel
import com.epam.harrypotterspells.utils.schedulers.SchedulerProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val schedulerProvider: SchedulerProvider,
    private val switchToRemoteUseCase: UseCase<SwitchToRemoteAction, SwitchToRemoteResult>,
    private val switchToLocalUseCase: UseCase<SwitchToLocalAction, SwitchToLocalResult>,
) : ViewModel(), MVIViewModel<MainIntent, MainViewState> {

    private val intentsSubject = BehaviorSubject.create<MainIntent>()
    private val initialState = MainViewState(isRemote = true)
    private val statesObservable = compose()

    private fun compose(): Observable<MainViewState> {
        return intentsSubject
            .observeOn(schedulerProvider.computation())
            .map(this::getActionFromIntent)
            .compose(processActions())
            .scan(initialState, reducer)
            .observeOn(schedulerProvider.ui())
            .replay()
            .autoConnect()
            .distinctUntilChanged()
    }

    private fun processActions() =
        ObservableTransformer<MainAction, MainResult> { actions ->
            Observable.merge(
                actions.ofType(SwitchToRemoteAction::class.java).compose(
                    switchToRemoteUseCase.performAction()
                ),
                actions.ofType(SwitchToLocalAction::class.java).compose(
                    switchToLocalUseCase.performAction()
                ),
            )
        }

    override fun processIntents(observable: Observable<MainIntent>) {
        observable.subscribe(intentsSubject)
    }

    override fun getStates(): Observable<MainViewState> = statesObservable

    private fun getActionFromIntent(intent: MainIntent) = when (intent) {
        is SwitchToRemoteIntent -> SwitchToRemoteAction
        is SwitchToLocalIntent -> SwitchToLocalAction
    }

    private companion object {
        private val reducer = BiFunction { _:MainViewState, result: MainResult ->
            when (result) {
                is SwitchToRemoteResult -> MainViewState(isRemote = true)
                is SwitchToLocalResult -> MainViewState(isRemote = false)
            }
        }
    }
}