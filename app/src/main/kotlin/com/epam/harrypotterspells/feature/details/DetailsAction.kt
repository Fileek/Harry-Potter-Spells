package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class DetailsAction : MVIAction {
    sealed class EditAction : DetailsAction() {
        object IncantationAction : EditAction()
        object TypeAction : EditAction()
        object EffectAction : EditAction()
        object LightAction : EditAction()
        object CreatorAction : EditAction()
    }

    sealed class UpdateAction : DetailsAction() {
        data class IncantationAction(val id: String, val incantation: String) : UpdateAction()
        data class TypeAction(val id: String, val type: String) : UpdateAction()
        data class EffectAction(val id: String, val effect: String) : UpdateAction()
        data class LightAction(val id: String, val light: String) : UpdateAction()
        data class CreatorAction(val id: String, val creator: String) : UpdateAction()
    }
}
