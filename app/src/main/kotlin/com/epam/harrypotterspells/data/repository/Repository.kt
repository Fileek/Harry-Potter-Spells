package com.epam.harrypotterspells.data.repository

import com.epam.harrypotterspells.entity.SpannedSpell
import io.reactivex.rxjava3.core.Observable

interface Repository {

    fun getSpells(): Observable<List<SpannedSpell>>

    fun searchByQuery(query: String)

    fun switchToRemote()
    fun switchToLocal()

    fun updateIncantation(spellId: String, incantation: String)
    fun updateType(spellId: String, type: String)
    fun updateEffect(spellId: String, effect: String)
    fun updateLight(spellId: String, light: String)
    fun updateCreator(spellId: String, creator: String)
}