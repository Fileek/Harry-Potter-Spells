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

    fun updateIncantation(spellId: String, incantation: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(incantation = incantation)
        replaceSpell(oldSpell, newSpell)
    }

    fun updateType(spellId: String, type: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(type = type)
        replaceSpell(oldSpell, newSpell)
    }

    fun updateEffect(spellId: String, effect: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(effect = effect)
        replaceSpell(oldSpell, newSpell)
    }

    fun updateLight(spellId: String, light: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(light = light)
        replaceSpell(oldSpell, newSpell)
    }

    fun updateCreator(spellId: String, creator: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(creator = creator)
        replaceSpell(oldSpell, newSpell)
    }

    private fun findSpellById(id: String): Spell? {
        return spells.find { it.id == id }
    }

    private fun replaceSpell(oldSpell: Spell?, newSpell: Spell?) {
        Collections.replaceAll(spells, oldSpell, newSpell)
    }
}