package com.epam.harrypotterspells.main

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class MainAction : MVIAction {
    object SwitchToRemoteAction : MainAction()
    object SwitchToLocalAction : MainAction()
}