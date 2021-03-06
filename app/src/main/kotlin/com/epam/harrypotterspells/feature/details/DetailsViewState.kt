package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    val spell: Spell? = null,
    val isInitial: Boolean = true,
    val editableFields: Set<SpellField> = setOf(),
    val focus: SpellField? = null,
    val error: Throwable? = null,
) : MVIViewState