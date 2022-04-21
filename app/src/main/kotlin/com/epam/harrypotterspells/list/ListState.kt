package com.epam.harrypotterspells.list

import com.epam.harrypotterspells.network.Spell
import com.epam.harrypotterspells.redux.State

sealed class ListState : State {
    object Empty : ListState()
    object Loading : ListState()
    class Success(val spells: List<Spell>) : ListState()
    class Failure(val error: Throwable) : ListState()
}