package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIViewState

data class MainViewState(
    val isRemote: Boolean,
    val isNotSearching: Boolean
) : MVIViewState
