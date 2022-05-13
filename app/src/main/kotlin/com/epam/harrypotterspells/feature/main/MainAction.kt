package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class MainAction : MVIAction {
    sealed class SwitchSourceAction : MainAction() {
        object ToRemoteAction : SwitchSourceAction()
        object ToLocalAction : SwitchSourceAction()
    }

    sealed class SearchAction : MainAction() {
        object OpenAction : SearchAction()
        data class QueryAction(val query: String) : SearchAction()
        object CloseAction : SearchAction()
    }
}