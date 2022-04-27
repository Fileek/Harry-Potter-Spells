package com.epam.harrypotterspells.details

import com.epam.harrypotterspells.mvibase.MVIViewState

data class DetailsViewState(
    var incantationIsEditing: Boolean,
    var creatorIsEditing: Boolean
) : MVIViewState