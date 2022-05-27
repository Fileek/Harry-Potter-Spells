package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class SpellsViewState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val data: List<Spell> = emptyList(),
    val error: Throwable? = null,
) : MVIViewState