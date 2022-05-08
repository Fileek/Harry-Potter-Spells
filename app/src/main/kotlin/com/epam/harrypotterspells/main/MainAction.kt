package com.epam.harrypotterspells.main

sealed class MainAction {
    object SwitchToRemoteAction : MainAction()
    object SwitchToLocalAction : MainAction()
}