package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult
import com.epam.harrypotterspells.util.`typealias`.EditActionTransformer
import io.reactivex.rxjava3.core.Observable

class EditUseCase : UseCase<EditAction, EditResult> {

    override fun performAction() = EditActionTransformer {
        it.flatMap { action ->
            Observable.just(
                when (action) {
                    is EditAction.IncantationAction -> EditResult.IncantationResult
                    is EditAction.TypeAction -> EditResult.TypeResult
                    is EditAction.EffectAction -> EditResult.EffectResult
                    is EditAction.LightAction -> EditResult.LightResult
                    is EditAction.CreatorAction -> EditResult.CreatorResult
                }
            )
        }
    }
}