package com.epam.harrypotterspells.spells

import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class SpellsResult : MVIResult {
    sealed class LoadSpellsResult : SpellsResult() {
        data class Success(val data: List<Spell>) : LoadSpellsResult()
        object Loading : LoadSpellsResult()
        data class Error(val error: Throwable) : LoadSpellsResult()
    }
}
