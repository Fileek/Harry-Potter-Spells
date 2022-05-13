package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.mvibase.MVIResult

sealed class SpellsResult : MVIResult {
    sealed class LoadResult : SpellsResult() {
        data class Success(val data: List<SpannedSpell>) : LoadResult()
        object Loading : LoadResult()
        data class Error(val error: Throwable) : LoadResult()
    }
}
