package com.epam.harrypotterspells.data.api

import com.epam.harrypotterspells.entity.JsonSpell
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface SpellApi {

    @GET("/Spells")
    fun getSpells(): Single<List<JsonSpell>>
}