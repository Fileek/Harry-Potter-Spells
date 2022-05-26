package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIAction

sealed class DetailsAction : MVIAction {
    data class SwitchFieldToEditableAction(val field: SpellField) : DetailsAction()
    data class SaveSpellFieldAction(val spell: Spell, val field: SpellField) : DetailsAction()
}
