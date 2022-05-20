package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class MainIntent : MVIIntent {
    object SwitchToRemoteIntent : MainIntent()
    object SwitchToLocalIntent : MainIntent()
}
