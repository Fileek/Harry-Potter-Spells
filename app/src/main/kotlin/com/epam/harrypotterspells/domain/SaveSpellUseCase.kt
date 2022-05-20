package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.details.DetailsAction.SaveSpellAction
import com.epam.harrypotterspells.feature.details.DetailsResult.SaveSpellFieldResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class SaveSpellUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : UseCase<SaveSpellAction, SaveSpellFieldResult> {

    override fun performAction() =
        ObservableTransformer<SaveSpellAction, SaveSpellFieldResult> {
            it.flatMap { action ->
                localRepository.saveSpell(action.spell)
                remoteRepository.saveSpell(action.spell)
                Observable.just(SaveSpellFieldResult(action.spell, action.field))
            }
        }
}