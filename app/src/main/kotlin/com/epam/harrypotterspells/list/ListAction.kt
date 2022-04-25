package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.Spell
import com.epam.harrypotterspells.redux.Action

sealed class ListAction : Action {
    object Loading : ListAction()
    class Edit(val spell: Spell) : ListAction()
    class Success(val spells: List<Spell>) : ListAction()
    class Failure(val error: Throwable) : ListAction()
}
