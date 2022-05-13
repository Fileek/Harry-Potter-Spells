package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class SpellsAction : MVIAction {
    object LoadAction : SpellsAction()
}
