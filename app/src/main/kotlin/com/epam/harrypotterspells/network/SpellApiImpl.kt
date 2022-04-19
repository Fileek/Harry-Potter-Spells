package com.epam.harrypotterspells.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpellApiImpl {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://wizard-world-api.herokuapp.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private val spellService = retrofit.create(SpellApi::class.java)

    fun getCats(): Observable<List<Spell>> {
        return spellService.getSpells()
    }
}