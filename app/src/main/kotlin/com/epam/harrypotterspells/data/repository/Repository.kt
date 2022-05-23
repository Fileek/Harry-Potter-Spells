package com.epam.harrypotterspells.data.repository

import com.epam.harrypotterspells.entity.Spell
import io.reactivex.rxjava3.core.Single
import java.util.Collections

/**
 * Holds all data, provides it by [getSpells] method and can update it by [saveSpell] method.
 */
abstract class Repository {

    protected open var spells: List<Spell> = emptyList()

    /**
     * The first use will start loading spells from the API or a local source
     * depending on the implementation and return the result.
     * On subsequent uses, it will return the previously loaded list.
     */
    fun getSpells(): Single<List<Spell>> {
        return if (spells.isNotEmpty()) Single.just(spells)
        else loadSpells()
    }

    protected abstract fun loadSpells(): Single<List<Spell>>

    /**
     * Saves spell by replacing the previous spell with the same [Spell.id] in [spells].
     * @param [newSpell] spell to replace.
     */
    fun saveSpell(newSpell: Spell) {
        val oldSpell = spells.find { it.id == newSpell.id }
        Collections.replaceAll(spells, oldSpell, newSpell)
    }
}