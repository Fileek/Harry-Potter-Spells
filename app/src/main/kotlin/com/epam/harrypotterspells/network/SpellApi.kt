package com.epam.harrypotterspells.network

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface SpellApi {
    @GET("/Spells")
    fun getSpells(): Observable<List<Spell>>
}