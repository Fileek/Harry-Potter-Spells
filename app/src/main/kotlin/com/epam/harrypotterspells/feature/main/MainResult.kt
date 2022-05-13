package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class MainResult : MVIResult {
    sealed class SwitchSourceResult : MainResult() {
        object ToRemoteResult : SwitchSourceResult()
        object ToLocalResult : SwitchSourceResult()
    }

    sealed class SearchResult : MainResult() {
        object OpenResult : SearchResult()
        object QueryResult : SearchResult()
        object CloseResult : SearchResult()
    }
}