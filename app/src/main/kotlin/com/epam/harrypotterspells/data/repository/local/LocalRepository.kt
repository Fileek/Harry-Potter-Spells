package com.epam.harrypotterspells.data.repository.local

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.entity.Spell
import io.reactivex.rxjava3.core.Single

class LocalRepository : Repository() {

    override fun loadSpells(): Single<List<Spell>> {
        spells = StubList.spells.map { it.toSpell() }
        return Single.just(spells)
    }
}