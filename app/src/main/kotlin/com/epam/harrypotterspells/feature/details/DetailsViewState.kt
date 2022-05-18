package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    var spell: Spell? = null,
    var inputsTextsNotSet: Boolean = true,
    var incantationIsEditing: Boolean = false,
    var typeIsEditing: Boolean = false,
    var effectIsEditing: Boolean = false,
    var lightIsEditing: Boolean = false,
    var creatorIsEditing: Boolean = false,
    var focus: SpellFieldFocus = SpellFieldFocus.NONE,
    var error: Throwable? = null,
) : MVIViewState