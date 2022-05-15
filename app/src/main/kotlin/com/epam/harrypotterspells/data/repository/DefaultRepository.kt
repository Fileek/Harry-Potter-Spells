package com.epam.harrypotterspells.data.repository

import android.text.SpannedString
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.local.StubList
import com.epam.harrypotterspells.entity.JsonSpell
import com.epam.harrypotterspells.entity.SpannedSpell
import com.epam.harrypotterspells.util.extension.filterByStringAndHighlightIt
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.Collections
import javax.inject.Inject

class DefaultRepository @Inject constructor(
    private val api: SpellApi,
    private val schedulerProvider: SchedulerProvider
) : Repository {

    private var isRemote = true
    private var searchQuery = ""
    private val localSpells = StubList.spells.map { it.toSpannedSpell() }
    private var remoteSpells = emptyList<SpannedSpell>()
    private val spells
        get() = if (isRemote) remoteSpells else localSpells
    private val filteredSpells
        get() = spells.filterByStringAndHighlightIt(searchQuery, ignoreCase = true)

    private val spellsSubject = BehaviorSubject.create<List<SpannedSpell>>()

    override fun getSpells(): Observable<List<SpannedSpell>> {
        api.getSpells()
            .subscribeOn(schedulerProvider.io())
            .subscribe(::processSuccessResponse, ::processErrorResponse)
        return spellsSubject.serialize()
    }

    private fun processSuccessResponse(data: List<JsonSpell>) {
        val list = data.map { it.toSpannedSpell() }
        remoteSpells = list
        emitSpells()
    }

    private fun processErrorResponse(error: Throwable) {
        remoteSpells = localSpells
        emitSpells()
    }

    private fun emitSpells() {
        spellsSubject.onNext(filteredSpells)
    }

    override fun searchByQuery(query: String) {
        searchQuery = query
        emitSpells()
    }

    override fun switchSourceToRemote() {
        isRemote = true
        if (spells.isNotEmpty()) emitSpells()
    }

    override fun switchSourceToLocal() {
        isRemote = false
        emitSpells()
    }

    override fun updateIncantation(spellId: String, incantation: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(incantation = SpannedString(incantation))
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateType(spellId: String, type: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(type = SpannedString(type))
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateEffect(spellId: String, effect: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(effect = SpannedString(effect))
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateLight(spellId: String, light: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(light = light)
        replaceSpell(oldSpell, newSpell)
    }

    override fun updateCreator(spellId: String, creator: String) {
        val oldSpell = findSpellById(spellId)
        val newSpell = oldSpell?.copy(creator = creator)
        replaceSpell(oldSpell, newSpell)
    }

    private fun findSpellById(id: String): SpannedSpell? {
        return spells.find { it.id == id }
    }

    private fun replaceSpell(oldSpell: SpannedSpell?, newSpell: SpannedSpell?) {
        Collections.replaceAll(spells, oldSpell, newSpell)
        emitSpells()
    }
}