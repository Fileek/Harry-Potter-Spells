package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import com.epam.harrypotterspells.util.`typealias`.UpdateActionTransformer
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UpdateUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<UpdateAction, UpdateResult> {

    override fun performAction() = UpdateActionTransformer {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is UpdateAction.IncantationAction -> {
                        repository.updateIncantation(action.id, action.incantation)
                        UpdateResult.IncantationResult(action.incantation)
                    }
                    is UpdateAction.TypeAction -> {
                        repository.updateType(action.id, action.type)
                        UpdateResult.TypeResult(action.type)
                    }
                    is UpdateAction.EffectAction -> {
                        repository.updateEffect(action.id, action.effect)
                        UpdateResult.EffectResult(action.effect)
                    }
                    is UpdateAction.LightAction -> {
                        repository.updateLight(action.id, action.light)
                        UpdateResult.LightResult(action.light)
                    }
                    is UpdateAction.CreatorAction -> {
                        repository.updateCreator(action.id, action.creator)
                        UpdateResult.CreatorResult(action.creator)
                    }
                }
            )
        }
    }
}