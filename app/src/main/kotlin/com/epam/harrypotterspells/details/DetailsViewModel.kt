package com.epam.harrypotterspells.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.ext.toJsonSpell
import com.epam.harrypotterspells.ext.toSpell
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository,
    state: SavedStateHandle,
) : ViewModel() {

    val state = DetailsViewState()
    var spell: Spell = state.get<Spell>(SPELL_KEY) ?: repository.spellsStub.first().toSpell()
        private set

    fun updateSpell(updatedSpell: Spell) {
        spell = updatedSpell
        repository.editSpell(updatedSpell.toJsonSpell())
    }

    private companion object {
        // The constant must match the name of the argument in nav_graph
        private const val SPELL_KEY = "spell"
    }
}