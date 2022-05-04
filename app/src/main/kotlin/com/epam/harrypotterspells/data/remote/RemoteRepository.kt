package com.epam.harrypotterspells.data.remote

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entities.JsonSpell
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val api: SpellApi
) : Repository {

    override val spellsStub = StubList.spells

    private val spellsSubject = BehaviorSubject.create<List<JsonSpell>>()

    override fun getSpells(): Observable<List<JsonSpell>> {
        api.getSpells()
            .subscribeOn(Schedulers.io())
            .subscribe(::processSuccessResponse, ::processErrorResponse)
        return spellsSubject.serialize()
    }

    private fun processSuccessResponse(data: List<JsonSpell>) {
        spellsSubject.onNext(data)
    }

    private fun processErrorResponse(error: Throwable) {
        spellsSubject.onNext(spellsStub)
    }

    override fun editSpell(newSpell: JsonSpell) {
        val spellsList = spellsSubject.value ?: spellsStub
        val oldSpell = spellsList.find { spell -> spell.id == newSpell.id }
        Collections.replaceAll(spellsList, oldSpell, newSpell)
        spellsSubject.onNext(spellsList)
    }
}