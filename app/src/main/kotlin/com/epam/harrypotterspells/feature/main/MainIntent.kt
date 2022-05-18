package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class MainIntent : MVIIntent {
    sealed class SwitchSourceIntent : MainIntent() {
        object ToRemoteIntent : SwitchSourceIntent()
        object ToLocalIntent : SwitchSourceIntent()
    }

    data class SearchByQueryIntent(val query: String) : MainIntent()
}
