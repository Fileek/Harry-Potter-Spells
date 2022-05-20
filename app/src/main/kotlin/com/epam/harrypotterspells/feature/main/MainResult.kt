package com.epam.harrypotterspells.feature.main

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class MainResult : MVIResult {
    object SwitchToRemoteResult : MainResult()
    object SwitchToLocalResult : MainResult()
}