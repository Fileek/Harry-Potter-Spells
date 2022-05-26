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
 * [Repository] implementation that uses given [api]
 * and [schedulerProvider] to retrieve data from network.
 * @param api API for network requests.
 * @param schedulerProvider [SchedulerProvider] for network requests.
 */
class RemoteRepository @Inject constructor(
    private val api: SpellApi,
    private val schedulerProvider: SchedulerProvider
) : Repository {

    override fun getSpells(): Single<List<JsonSpell>> {
        return api.getSpells()
            .subscribeOn(schedulerProvider.getIOScheduler())
            .onErrorReturn {
                StubList.spells
            }
    }
}