package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class SpellsViewState(
    val isLoading: Boolean = false,
    val data: List<SpannedSpell> = emptyList(),
    val error: Throwable? = null,
) : MVIViewState