package com.epam.harrypotterspells.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpellApiImpl : SpellApi {
    private const val BASE_URL = "https://wizard-world-api.herokuapp.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val spellService = retrofit.create(SpellApi::class.java)

    override fun getSpells(): Observable<List<Spell>> {
        return spellService.getSpells()
    }
}