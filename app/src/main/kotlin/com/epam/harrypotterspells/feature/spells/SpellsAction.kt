package com.epam.harrypotterspells.feature.spells

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIAction

sealed class SpellsAction : MVIAction {
    object GetRemoteAction: SpellsAction()
    object GetCacheAction: SpellsAction()
    object GetLocalAction : SpellsAction()
    data class SaveSpellAction(val spell: Spell) : SpellsAction()
}
