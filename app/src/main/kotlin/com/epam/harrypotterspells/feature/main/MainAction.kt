package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class MainAction : MVIAction {
    object SwitchViewToRemoteAction : MainAction()
    object SwitchViewToLocalAction : MainAction()
}