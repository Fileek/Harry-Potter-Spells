package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class DetailsIntent : MVIIntent {

    sealed class EditSpellIntent : DetailsIntent() {
        object EditIncantationIntent : EditSpellIntent()
        object EditTypeIntent : EditSpellIntent()
        object EditEffectIntent : EditSpellIntent()
        object EditLightIntent : EditSpellIntent()
        object EditCreatorIntent : EditSpellIntent()
    }

    sealed class UpdateSpellIntent : DetailsIntent() {
        data class UpdateIncantationIntent(
            val id: String,
            val incantation: String
        ) : UpdateSpellIntent()

        data class UpdateTypeIntent(
            val id: String,
            val type: String
        ) : UpdateSpellIntent()

        data class UpdateEffectIntent(
            val id: String,
            val effect: String
        ) : UpdateSpellIntent()

        data class UpdateLightIntent(
            val id: String,
            val light: String
        ) : UpdateSpellIntent()

        data class UpdateCreatorIntent(
            val id: String,
            val creator: String
        ) : UpdateSpellIntent()
    }
}
