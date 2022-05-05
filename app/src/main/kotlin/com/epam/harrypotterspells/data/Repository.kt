package com.epam.harrypotterspells.data

import com.epam.harrypotterspells.entities.JsonSpell
import io.reactivex.rxjava3.core.Observable

interface Repository {

    val spellsStub: List<JsonSpell>

    fun getSpells(): Observable<List<JsonSpell>>

    fun getSpellById(id: String): Observable<JsonSpell>

    fun updateIncantation(id: String, incantation: String)
    fun updateType(id: String, type: String)
    fun updateEffect(id: String, effect: String)
    fun updateLight(id: String, light: String)
    fun updateCreator(id: String, creator: String)
}