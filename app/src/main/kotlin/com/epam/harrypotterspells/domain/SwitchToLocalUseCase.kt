package com.epam.harrypotterspells.domain

import android.util.Log
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.ext.TAG
import com.epam.harrypotterspells.main.MainAction.SwitchToLocalAction
import com.epam.harrypotterspells.main.MainResult.SwitchToLocalResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SwitchToLocalUseCase @Inject constructor(
    private val repository: Repository
) {
    fun performAction() =
        ObservableTransformer<SwitchToLocalAction, SwitchToLocalResult> {
            it.flatMap {
                repository.switchToLocal()
                Observable.just(SwitchToLocalResult)
            }
        }
}