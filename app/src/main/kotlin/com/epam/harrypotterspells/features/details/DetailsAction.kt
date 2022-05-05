package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class DetailsAction : MVIAction {
    data class GetSpellAction(val id: String) : DetailsAction()

    sealed class EditSpellAction : DetailsAction() {
        object EditIncantationAction : EditSpellAction()
        object EditTypeAction : EditSpellAction()
        object EditEffectAction : EditSpellAction()
        object EditLightAction : EditSpellAction()
        object EditCreatorAction : EditSpellAction()
    }

    sealed class UpdateSpellAction : DetailsAction() {
        data class UpdateIncantationAction(
            val id: String,
            val incantation: String
        ) : UpdateSpellAction()

        data class UpdateTypeAction(
            val id: String,
            val type: String
        ) : UpdateSpellAction()

        data class UpdateEffectAction(
            val id: String,
            val effect: String
        ) : UpdateSpellAction()

        data class UpdateLightAction(
            val id: String,
            val light: String
        ) : UpdateSpellAction()

        data class UpdateCreatorAction(
            val id: String,
            val creator: String
        ) : UpdateSpellAction()
    }
}
