package com.epam.harrypotterspells.details

import androidx.lifecycle.ViewModel
import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.entities.Spell
import com.epam.harrypotterspells.ext.toJsonSpell
import com.epam.harrypotterspells.ext.toSpell
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val state = DetailsViewState()

    fun getSpellById(id: String): Spell {
        return repository.getSpellById(id).toSpell()
    }

    fun editSpell(spell: Spell) {
        repository.editSpell(spell.toJsonSpell())
    }
}