package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class DetailsIntent : MVIIntent {
    sealed class EditIntent : DetailsIntent() {
        object IncantationIntent : EditIntent()
        object TypeIntent : EditIntent()
        object EffectIntent : EditIntent()
        object LightIntent : EditIntent()
        object CreatorIntent : EditIntent()
    }

    sealed class UpdateIntent : DetailsIntent() {
        data class IncantationIntent(val id: String, val incantation: String) : UpdateIntent()
        data class TypeIntent(val id: String, val type: String) : UpdateIntent()
        data class EffectIntent(val id: String, val effect: String) : UpdateIntent()
        data class LightIntent(val id: String, val light: String) : UpdateIntent()
        data class CreatorIntent(val id: String, val creator: String) : UpdateIntent()
    }
}
