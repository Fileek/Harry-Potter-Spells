package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class DetailsResult : MVIResult {

    sealed class EditSpellResult : DetailsResult() {
        object EditIncantationResult : EditSpellResult()
        object EditTypeResult : EditSpellResult()
        object EditEffectResult : EditSpellResult()
        object EditLightResult : EditSpellResult()
        object EditCreatorResult : EditSpellResult()
    }

    sealed class UpdateSpellResult : DetailsResult() {
        data class UpdateIncantationResult(val incantation: String) : UpdateSpellResult()
        data class UpdateTypeResult(val type: String) : UpdateSpellResult()
        data class UpdateEffectResult(val effect: String) : UpdateSpellResult()
        data class UpdateLightResult(val light: String) : UpdateSpellResult()
        data class UpdateCreatorResult(val creator: String) : UpdateSpellResult()
    }
}
