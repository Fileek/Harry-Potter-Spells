package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class SpellsResult : MVIResult {
    sealed class RemoteResult : SpellsResult() {
        object Loading : RemoteResult()
        data class Success(val data: List<Spell>) : RemoteResult()
        data class Error(val error: Throwable) : RemoteResult()
    }

    data class LocalResult(val data: List<Spell>) : SpellsResult()
}
