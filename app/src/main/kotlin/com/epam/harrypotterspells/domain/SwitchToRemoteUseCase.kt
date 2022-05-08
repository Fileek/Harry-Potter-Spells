package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.main.MainAction.SwitchToRemoteAction
import com.epam.harrypotterspells.main.MainResult.SwitchToRemoteResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SwitchToRemoteUseCase @Inject constructor(
    private val repository: Repository
) {
    fun performAction() =
        ObservableTransformer<SwitchToRemoteAction, SwitchToRemoteResult> {
            it.flatMap {
                repository.switchToRemote()
                Observable.just(SwitchToRemoteResult)
            }
        }
}