package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class DetailsResult : MVIResult {
    sealed class GetSpellResult : DetailsResult() {
        data class Success(val spell: Spell) : GetSpellResult()
    }

    sealed class EditSpellResult : DetailsResult() {
        object EditIncantationResult : EditSpellResult()
        object EditTypeResult : EditSpellResult()
        object EditEffectResult : EditSpellResult()
        object EditLightResult : EditSpellResult()
        object EditCreatorResult : EditSpellResult()
    }

    sealed class UpdateSpellResult : DetailsResult() {
        object UpdateIncantationResult : UpdateSpellResult()
        object UpdateTypeResult : UpdateSpellResult()
        object UpdateEffectResult : UpdateSpellResult()
        object UpdateLightResult : UpdateSpellResult()
        object UpdateCreatorResult : UpdateSpellResult()
    }
}
