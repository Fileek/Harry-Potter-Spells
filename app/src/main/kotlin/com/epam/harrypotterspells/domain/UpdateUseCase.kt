package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class UpdateUseCase @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : UseCase<UpdateAction, UpdateResult> {

    override fun performAction() = ObservableTransformer<UpdateAction, UpdateResult> {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is UpdateAction.IncantationAction -> {
                        localRepository.updateIncantation(action.id, action.incantation)
                        remoteRepository.updateIncantation(action.id, action.incantation)
                        UpdateResult.IncantationResult(action.incantation)
                    }
                    is UpdateAction.TypeAction -> {
                        localRepository.updateType(action.id, action.type)
                        remoteRepository.updateType(action.id, action.type)
                        UpdateResult.TypeResult(action.type)
                    }
                    is UpdateAction.EffectAction -> {
                        localRepository.updateEffect(action.id, action.effect)
                        remoteRepository.updateEffect(action.id, action.effect)
                        UpdateResult.EffectResult(action.effect)
                    }
                    is UpdateAction.LightAction -> {
                        localRepository.updateLight(action.id, action.light)
                        remoteRepository.updateLight(action.id, action.light)
                        UpdateResult.LightResult(action.light)
                    }
                    is UpdateAction.CreatorAction -> {
                        localRepository.updateCreator(action.id, action.creator)
                        remoteRepository.updateCreator(action.id, action.creator)
                        UpdateResult.CreatorResult(action.creator)
                    }
                }
            )
        }
    }
}