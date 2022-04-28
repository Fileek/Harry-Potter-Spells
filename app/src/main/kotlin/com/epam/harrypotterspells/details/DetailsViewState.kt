package com.epam.harrypotterspells.details

import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    var incantationIsEditing: Boolean = false,
    var typeIsEditing: Boolean = false,
    var effectIsEditing: Boolean = false,
    var lightIsEditing: Boolean = false,
    var creatorIsEditing: Boolean = false
) : MVIViewState