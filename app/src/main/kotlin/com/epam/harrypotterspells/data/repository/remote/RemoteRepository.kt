package com.epam.harrypotterspells.data.repository.remote

import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.repository.local.StubList
import com.epam.harrypotterspells.entity.JsonSpell
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Remote realisation of [Repository] that provides data from given [api].
 * @param api data source for the repository.
 * @param schedulerProvider [SchedulerProvider] for network requests.
 */
class RemoteRepository @Inject constructor(
    private val api: SpellApi,
    private val schedulerProvider: SchedulerProvider
) : Repository() {

    override fun loadSpells(): Single<List<Spell>> {
        return api.getSpells()
            .subscribeOn(schedulerProvider.getIOScheduler())
            .map(::processSuccessResponse)
            .onErrorReturn(::processErrorResponse)
    }

    private fun processSuccessResponse(data: List<JsonSpell>): List<Spell> {
        spells = data.map { it.toSpell() }
        return spells
    }

    private fun processErrorResponse(error: Throwable): List<Spell> {
        spells = StubList.spells.map { it.toSpell() }
        return spells
    }
}