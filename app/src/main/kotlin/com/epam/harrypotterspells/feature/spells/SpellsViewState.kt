package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.mvibase.MVIViewState

sealed class SpellsViewState(
    val isLoading: Boolean,
    val data: List<SpannedSpell>,
    val error: Throwable?,
) : MVIViewState {

    data class Success(
        val spells: List<SpannedSpell>
    ) : SpellsViewState(false, spells, null)

    data class Error(
        val throwable: Throwable
    ) : SpellsViewState(false, emptyList(), throwable)

    object Loading :
        SpellsViewState(true, emptyList(), null)

    object Idle :
        SpellsViewState(false, emptyList(), null)
}