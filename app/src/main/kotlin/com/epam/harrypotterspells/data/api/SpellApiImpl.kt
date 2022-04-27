package com.epam.harrypotterspells.data.api

import com.epam.harrypotterspells.entities.Spell
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import javax.inject.Inject

class SpellApiImpl @Inject constructor(
    retrofit: Retrofit
) : SpellApi {

    private val spellService = retrofit.create(SpellApi::class.java)

    override fun getSpells(): Single<List<Spell>> {
        return spellService.getSpells()
    }
}