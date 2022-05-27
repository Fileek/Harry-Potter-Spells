package com.epam.harrypotterspells.data.repository.local

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.entity.JsonSpell
import com.epam.harrypotterspells.entity.Spell
import io.reactivex.rxjava3.core.Single
import java.util.Collections

/**
 * [Repository] implementation that works with stub data.
 */
class LocalRepository : Repository {

    private val stubSpells = StubData.spells

    override fun getSpells(): Single<List<JsonSpell>> = Single.just(stubSpells)

    /**
     * Saves spell by replacing the previous spell with the same [Spell.id] in the stub list.
     * @param [newSpell] spell to replace.
     */
    fun saveSpell(newSpell: JsonSpell) {
        val oldSpell = stubSpells.find { it.id == newSpell.id }
        Collections.replaceAll(stubSpells, oldSpell, newSpell)
    }
}