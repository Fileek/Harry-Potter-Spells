package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SwitchSourceUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<SwitchSourceAction, SwitchSourceResult> {

    override fun performAction() = ObservableTransformer<SwitchSourceAction, SwitchSourceResult> {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is SwitchSourceAction.ToRemoteAction -> {
                        repository.switchSourceToRemote()
                        SwitchSourceResult.ToRemoteResult
                    }
                    is SwitchSourceAction.ToLocalAction -> {
                        repository.switchSourceToLocal()
                        SwitchSourceResult.ToLocalResult
                    }
                }
            )
        }
    }
}