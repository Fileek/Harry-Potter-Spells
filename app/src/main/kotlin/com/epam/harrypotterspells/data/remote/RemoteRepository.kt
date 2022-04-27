package com.epam.harrypotterspells.data.remote

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entities.Spell
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    api: SpellApi
) : Repository {

    override val spells: Observable<List<Spell>> get() = spellsSubject.serialize()

    private val spellsSubject = BehaviorSubject.create<List<Spell>>()
    private var spellsList = emptyList<Spell>()

    init {
        api.getSpells()
            .subscribeOn(Schedulers.io())
            .subscribe({
                processSuccessResponse(it)
            }, {
                processErrorResponse(it)
            })
    }

    private fun processSuccessResponse(data: List<Spell>) {
        spellsList = data
        spellsSubject.onNext(data)
    }

    private fun processErrorResponse(error: Throwable) {
        spellsList = StubList.spells
        spellsSubject.onNext(StubList.spells)
    }

    override fun getSpellById(id: String): Spell {
        return spellsList.find { spell -> spell.id == id } ?: StubList.spells.first()
    }

    override fun editSpell(newSpell: Spell) {
        val oldSpell = spellsList.find { spell -> spell.id == newSpell.id }
        Collections.replaceAll(spellsList, oldSpell, newSpell)
        spellsSubject.onNext(spellsList)
    }
}