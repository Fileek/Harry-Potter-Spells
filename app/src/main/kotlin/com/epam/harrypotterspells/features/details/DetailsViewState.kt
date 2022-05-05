package com.epam.harrypotterspells.features.details

import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.ext.toSpell
import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    var spell: Spell = StubList.spells.first().toSpell(),
    var error: Throwable? = null,
    var incantationIsEditing: Boolean = false,
    var typeIsEditing: Boolean = false,
    var effectIsEditing: Boolean = false,
    var lightIsEditing: Boolean = false,
    var creatorIsEditing: Boolean = false,
    var fieldFocus: FieldFocus = FieldFocus.NONE,
    var inputsNotInitialized: Boolean = true
) : MVIViewState