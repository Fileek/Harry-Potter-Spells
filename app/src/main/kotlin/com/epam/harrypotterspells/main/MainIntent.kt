package com.epam.harrypotterspells.main

import com.epam.harrypotterspells.mvibase.MVIIntent

sealed class MainIntent : MVIIntent {
    object SwitchToRemoteIntent : MainIntent()
    object SwitchToLocalIntent : MainIntent()
}
