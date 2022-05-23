package com.epam.harrypotterspells.feature.details

import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.mvibase.MVIAction

sealed class DetailsAction : MVIAction {
    /**
     * Action adds [field] to [DetailsViewState.fieldsNowEditing].
     * @param field [SpellField] user wants to edit.
     */
    data class AddInFieldsNowEditingAction(val field: SpellField) : DetailsAction()

    /**
     * Action saves [spell] in repositories
     * and removes [field] from [DetailsViewState.fieldsNowEditing].
     *
     * @param spell [Spell] to save
     * @param field changed [SpellField]
     */
    data class SaveSpellAction(val spell: Spell, val field: SpellField) : DetailsAction()
}
