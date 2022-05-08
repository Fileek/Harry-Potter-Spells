package com.epam.harrypotterspells.main

sealed class MainResult {
    object SwitchToRemoteResult : MainResult()
    object SwitchToLocalResult : MainResult()
}