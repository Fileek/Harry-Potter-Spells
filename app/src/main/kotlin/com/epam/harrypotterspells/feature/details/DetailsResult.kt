package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class DetailsResult : MVIResult {
    sealed class EditResult : DetailsResult() {
        object IncantationResult : EditResult()
        object TypeResult : EditResult()
        object EffectResult : EditResult()
        object LightResult : EditResult()
        object CreatorResult : EditResult()
    }

    sealed class UpdateResult : DetailsResult() {
        data class IncantationResult(val incantation: String) : UpdateResult()
        data class TypeResult(val type: String) : UpdateResult()
        data class EffectResult(val effect: String) : UpdateResult()
        data class LightResult(val light: String) : UpdateResult()
        data class CreatorResult(val creator: String) : UpdateResult()
    }
}
