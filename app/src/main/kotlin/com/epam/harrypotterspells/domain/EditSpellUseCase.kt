package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer

class EditSpellUseCase {

    fun performAction() =
        ObservableTransformer<EditSpellAction, EditSpellResult> {
            it.flatMap { action ->
                Observable.just(
                    when (action) {
                        is EditSpellAction.EditIncantationAction -> EditSpellResult.EditIncantationResult
                        is EditSpellAction.EditTypeAction -> EditSpellResult.EditTypeResult
                        is EditSpellAction.EditEffectAction -> EditSpellResult.EditEffectResult
                        is EditSpellAction.EditLightAction -> EditSpellResult.EditLightResult
                        is EditSpellAction.EditCreatorAction -> EditSpellResult.EditCreatorResult
                    }
                )
            }
        }
}