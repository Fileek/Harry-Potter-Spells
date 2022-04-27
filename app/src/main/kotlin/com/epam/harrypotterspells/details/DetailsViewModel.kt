package com.epam.harrypotterspells.details

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.entities.Spell
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val state = DetailsViewState(
        incantationIsEditing = false,
        creatorIsEditing = false
    )

    fun getSpellById(id: String): Spell {
        return repository.getSpellById(id)
    }

    fun editSpell(spell: Spell) {
        repository.editSpell(spell)
    }
}