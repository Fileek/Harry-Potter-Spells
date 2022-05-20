package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class SpellsResult : MVIResult {
    object Loading : SpellsResult()
    data class Success(val data: List<SpannedSpell>) : SpellsResult()
    data class Error(val error: Throwable) : SpellsResult()
}
