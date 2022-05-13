package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class MainIntent : MVIIntent {
    sealed class SwitchSourceIntent : MainIntent() {
        object ToRemoteIntent : SwitchSourceIntent()
        object ToLocalIntent : SwitchSourceIntent()
    }

    sealed class SearchIntent : MainIntent() {
        object OpenIntent : SearchIntent()
        data class QueryIntent(val query: String) : SearchIntent()
        object CloseIntent : SearchIntent()
    }
}
