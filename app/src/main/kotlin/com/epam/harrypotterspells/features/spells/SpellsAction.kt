package com.epam.harrypotterspells.features.spells

import com.epam.harrypotterspells.mvibase.MVIAction

sealed class SpellsAction : MVIAction {
    object LoadSpellsAction : SpellsAction()
}
