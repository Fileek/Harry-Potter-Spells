package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class MainResult : MVIResult {
    sealed class SwitchSourceResult : MainResult() {
        object ToRemoteResult : SwitchSourceResult()
        object ToLocalResult : SwitchSourceResult()
    }

    object SearchByQueryResult : MainResult()
}