package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.main.MainAction.SwitchSourceAction
import com.epam.harrypotterspells.feature.main.MainResult.SwitchSourceResult
import com.epam.harrypotterspells.util.`typealias`.SwitchSourceActionTransformer
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SwitchSourceUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<SwitchSourceAction, SwitchSourceResult> {

    override fun performAction() = SwitchSourceActionTransformer {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is SwitchSourceAction.ToRemoteAction -> {
                        repository.switchToRemote()
                        SwitchSourceResult.ToRemoteResult
                    }
                    is SwitchSourceAction.ToLocalAction -> {
                        repository.switchToLocal()
                        SwitchSourceResult.ToLocalResult
                    }
                }
            )
        }
    }
}