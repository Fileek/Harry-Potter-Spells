package com.epam.harrypotterspells.data.remote

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entities.JsonSpell
import com.epam.harrypotterspells.mvibase.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.Collections
import javax.inject.Inject

class RemoteRepository @Inject constructor(
    private val api: SpellApi,
    private val schedulerProvider: SchedulerProvider
) : Repository {

    private val spellsStub = StubList.spells

    private val spellsSubject = BehaviorSubject.create<List<JsonSpell>>()

    private var spellsList = emptyList<JsonSpell>()

    override fun getSpells(): Observable<List<JsonSpell>> {
        api.getSpells()
            .subscribeOn(schedulerProvider.io())
            .subscribe(::processSuccessResponse, ::processErrorResponse)
        return spellsSubject.serialize()
    }

    private fun processSuccessResponse(data: List<JsonSpell>) {
        spellsList = data
        spellsSubject.onNext(data)
    }

    private fun processErrorResponse(error: Throwable) {
        spellsList = spellsStub
        spellsSubject.onNext(spellsStub)
    }

    override fun switchToRemote() {
        spellsSubject.onNext(spellsList)
    }

    override fun switchToLocal() {
        spellsSubject.onNext(spellsStub)
    }

    override fun updateIncantation(id: String, incantation: String) {
        val oldSpell = spellsList.find { spell -> spell.id == id }
        val newSpell = oldSpell?.copy(incantation = incantation)
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateType(id: String, type: String) {
        val oldSpell = spellsList.find { spell -> spell.id == id }
        val newSpell = oldSpell?.copy(type = type)
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateEffect(id: String, effect: String) {
        val oldSpell = spellsList.find { spell -> spell.id == id }
        val newSpell = oldSpell?.copy(effect = effect)
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateLight(id: String, light: String) {
        val oldSpell = spellsList.find { spell -> spell.id == id }
        val newSpell = oldSpell?.copy(light = light)
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateCreator(id: String, creator: String) {
        val oldSpell = spellsList.find { spell -> spell.id == id }
        val newSpell = oldSpell?.copy(creator = creator)
        replaceSpell(oldSpell, newSpell)
    }

    private fun replaceSpell(oldSpell: JsonSpell?, newSpell: JsonSpell?) {
        Collections.replaceAll(spellsList, oldSpell, newSpell)
        spellsSubject.onNext(spellsList)
    }
}