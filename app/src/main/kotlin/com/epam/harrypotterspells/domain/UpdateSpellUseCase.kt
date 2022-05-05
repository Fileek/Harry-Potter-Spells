package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction.*
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class UpdateSpellUseCase @Inject constructor(
    private val repository: Repository
) {

    fun performAction() = ObservableTransformer<UpdateSpellAction, UpdateSpellResult> {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is UpdateIncantationAction -> {
                        repository.updateIncantation(action.id, action.incantation)
                        UpdateIncantationResult
                    }
                    is UpdateTypeAction -> {
                        repository.updateType(action.id, action.type)
                        UpdateTypeResult
                    }
                    is UpdateEffectAction -> {
                        repository.updateEffect(action.id, action.effect)
                        UpdateEffectResult
                    }
                    is UpdateLightAction -> {
                        repository.updateLight(action.id, action.light)
                        UpdateLightResult
                    }
                    is UpdateCreatorAction -> {
                        repository.updateCreator(action.id, action.creator)
                        UpdateCreatorResult
                    }
                }
            )
        }
    }
}