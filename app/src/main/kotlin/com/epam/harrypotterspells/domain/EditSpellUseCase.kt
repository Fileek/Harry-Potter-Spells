package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditCreatorAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditEffectAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditIncantationAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditLightAction
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction.EditTypeAction
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditCreatorResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditEffectResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditIncantationResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditLightResult
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult.EditTypeResult
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer

class EditSpellUseCase {

    fun performAction() =
        ObservableTransformer<EditSpellAction, EditSpellResult> {
            it.flatMap { action ->
                Observable.just(
                    when (action) {
                        is EditIncantationAction -> EditIncantationResult
                        is EditTypeAction -> EditTypeResult
                        is EditEffectAction -> EditEffectResult
                        is EditLightAction -> EditLightResult
                        is EditCreatorAction -> EditCreatorResult
                    }
                )
            }
        }
}