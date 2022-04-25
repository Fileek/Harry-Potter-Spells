package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.Spell
import com.epam.harrypotterspells.redux.Reducer
import java.util.*

class ListReducer : Reducer<ListState, ListAction> {

    override fun reduce(state: ListState, action: ListAction): ListState {
        return when (action) {
            is ListAction.Edit -> editSpellInState(state, action.spell)
            is ListAction.Loading -> ListState.Loading
            is ListAction.Success -> ListState.Success(action.spells)
            is ListAction.Failure -> ListState.Failure(action.error)
        }
    }

    private fun editSpellInState(state: ListState, newSpell: Spell): ListState {
        val spells = (state as? ListState.Success)?.spells ?: emptyList()
        val oldSpell = spells.find { it.name == newSpell.name }
        Collections.replaceAll(spells, oldSpell, newSpell)
        return ListState.Success(spells)
    }
}