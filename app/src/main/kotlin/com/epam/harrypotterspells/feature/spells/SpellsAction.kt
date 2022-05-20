package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class SpellsAction : MVIAction {
    sealed class LoadFilteredAction(val filter: String): SpellsAction() {
        class LoadRemoteAction(filter: String) : LoadFilteredAction(filter)
        class LoadLocalAction(filter: String) : LoadFilteredAction(filter)
    }
}
