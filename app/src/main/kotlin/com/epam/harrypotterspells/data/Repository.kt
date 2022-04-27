package com.epam.harrypotterspells.data

import com.epam.harrypotterspells.entities.Spell
import io.reactivex.rxjava3.core.Observable

interface Repository {

    val spells: Observable<List<Spell>>

    fun getSpellById(id: String): Spell

    fun editSpell(newSpell: Spell)
}