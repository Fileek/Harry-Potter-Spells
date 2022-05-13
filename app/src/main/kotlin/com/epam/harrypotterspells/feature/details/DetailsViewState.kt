package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    var spell: Spell? = null,
    var error: Throwable? = null,
    var incantationIsEditing: Boolean = false,
    var typeIsEditing: Boolean = false,
    var effectIsEditing: Boolean = false,
    var lightIsEditing: Boolean = false,
    var creatorIsEditing: Boolean = false,
    var focus: SpellFieldFocus = SpellFieldFocus.NONE,
    var inputsNotInitialized: Boolean = true
) : MVIViewState