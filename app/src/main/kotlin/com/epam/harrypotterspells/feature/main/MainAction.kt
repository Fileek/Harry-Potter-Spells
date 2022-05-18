package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class MainAction : MVIAction {
    sealed class SwitchSourceAction : MainAction() {
        object ToRemoteAction : SwitchSourceAction()
        object ToLocalAction : SwitchSourceAction()
    }

    data class SearchByQueryAction(val query: String) : MainAction()
}