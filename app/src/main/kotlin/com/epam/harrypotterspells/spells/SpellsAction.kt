package com.epam.harrypotterspells.spells

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class SpellsAction : MVIAction {
    object LoadSpellsAction : SpellsAction()
}
