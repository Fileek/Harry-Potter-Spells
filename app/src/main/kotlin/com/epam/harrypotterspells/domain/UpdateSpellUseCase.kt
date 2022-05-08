package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.UpdateTypeAction
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateCreatorResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateEffectResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateIncantationResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateLightResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.UpdateTypeResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class UpdateSpellUseCase @Inject constructor(
    private val repository: Repository
) {

    fun performAction() =
        ObservableTransformer<UpdateSpellAction, UpdateSpellResult> {
            it.flatMap { action ->
                Observable.just(
                    when (action) {
                        is UpdateIncantationAction -> performUpdateIncantationAction(action)
                        is UpdateTypeAction -> performUpdateTypeAction(action)
                        is UpdateEffectAction -> performUpdateEffectAction(action)
                        is UpdateLightAction -> performUpdateLightAction(action)
                        is UpdateCreatorAction -> performUpdateCreatorAction(action)
                    }
                )
            }
        }

    private fun performUpdateIncantationAction(action: UpdateIncantationAction): UpdateIncantationResult {
        repository.updateIncantation(action.id, action.incantation)
        return UpdateIncantationResult(action.incantation)
    }

    private fun performUpdateTypeAction(action: UpdateTypeAction): UpdateTypeResult {
        repository.updateType(action.id, action.type)
        return UpdateTypeResult(action.type)
    }

    private fun performUpdateEffectAction(action: UpdateEffectAction): UpdateEffectResult {
        repository.updateEffect(action.id, action.effect)
        return UpdateEffectResult(action.effect)
    }

    private fun performUpdateLightAction(action: UpdateLightAction): UpdateLightResult {
        repository.updateLight(action.id, action.light)
        return UpdateLightResult(action.light)
    }

    private fun performUpdateCreatorAction(action: UpdateCreatorAction): UpdateCreatorResult {
        repository.updateCreator(action.id, action.creator)
        return UpdateCreatorResult(action.creator)
    }
}