package com.epam.harrypotterspells.main

import com.epam.harrypotterspells.mvibase.MVIResult

sealed class MainResult : MVIResult {
    object SwitchToRemoteResult : MainResult()
    object SwitchToLocalResult : MainResult()
}