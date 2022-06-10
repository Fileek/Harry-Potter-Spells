package com.epam.harrypotterspells.data.repository

import com.epam.harrypotterspells.entity.JsonSpell
import io.reactivex.rxjava3.core.Single

/**
 * Represents data store.
 */
interface Repository {

    /**
     * Retrieve data from the repository as a stream with single value or error.
     */
    fun getSpells(): Single<List<JsonSpell>>
}