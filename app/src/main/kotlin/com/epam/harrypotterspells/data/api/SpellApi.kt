package com.epam.harrypotterspells.data.api

import com.epam.harrypotterspells.entity.JsonSpell
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

/**
 * Provides spells from Harry Potter Universe
 * using [Wizard World Api](https://wizard-world-api.herokuapp.com/swagger/index.html)
 */
interface SpellApi {

    /**
     * Provides list of [JsonSpell] from
     * [Wizard World Api](https://wizard-world-api.herokuapp.com/swagger/index.html)
     */
    @GET("/Spells")
    fun getSpells(): Single<List<JsonSpell>>
}