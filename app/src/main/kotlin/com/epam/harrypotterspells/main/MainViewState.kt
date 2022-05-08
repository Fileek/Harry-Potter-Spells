package com.epam.harrypotterspells.main

import com.epam.harrypotterspells.mvibase.MVIViewState

data class MainViewState(
    val isRemote: Boolean
) : MVIViewState
