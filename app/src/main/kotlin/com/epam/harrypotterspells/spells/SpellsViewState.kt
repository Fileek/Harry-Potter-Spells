package com.epam.harrypotterspells.spells

import com.epam.harrypotterspells.mvibase.MVIViewState
import com.epam.harrypotterspells.entities.Spell

sealed class SpellsViewState(
    val isLoading: Boolean,
    val data: List<Spell>,
    val error: Throwable?,
) : MVIViewState {

    data class Success(
        val spells: List<Spell>
    ) : SpellsViewState(false, spells, null)

    data class Error(
        val throwable: Throwable
    ) : SpellsViewState(false, emptyList(), throwable)

    object Loading :
        SpellsViewState(true, emptyList(), null)

    object Idle :
        SpellsViewState(false, emptyList(), null)
}