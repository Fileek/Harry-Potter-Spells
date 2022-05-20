package com.epam.harrypotterspells.data.repository

import com.epam.harrypotterspells.entity.Spell
import io.reactivex.rxjava3.core.Single
import java.util.Collections

abstract class Repository {

    protected open var spells: List<Spell> = emptyList()

    fun getSpells(): Single<List<Spell>> {
        return if (spells.isNotEmpty()) Single.just(spells)
        else loadSpells()
    }

    protected abstract fun loadSpells(): Single<List<Spell>>

    fun saveSpell(newSpell: Spell) {
        val oldSpell = spells.find { it.id == newSpell.id }
        Collections.replaceAll(spells, oldSpell, newSpell)
    }
}