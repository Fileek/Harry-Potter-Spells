package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class MainAction : MVIAction {
    object SwitchToRemoteAction : MainAction()
    object SwitchToLocalAction : MainAction()
}