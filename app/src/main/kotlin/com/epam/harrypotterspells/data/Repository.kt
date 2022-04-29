package com.epam.harrypotterspells.data

import com.epam.harrypotterspells.entities.JsonSpell
import io.reactivex.rxjava3.core.Observable

interface Repository {

    val spells: Observable<List<JsonSpell>>

    val spellsStub: List<JsonSpell>

    fun editSpell(newSpell: JsonSpell)
}