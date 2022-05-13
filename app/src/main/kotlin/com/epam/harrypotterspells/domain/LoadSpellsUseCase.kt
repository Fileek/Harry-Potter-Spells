package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LoadResult
import com.epam.harrypotterspells.util.`typealias`.LoadActionTransformer
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import javax.inject.Inject

class LoadSpellsUseCase @Inject constructor(
    private val repository: Repository,
    private val schedulerProvider: SchedulerProvider,
) : UseCase<LoadAction, LoadResult> {

    override fun performAction() = LoadActionTransformer {
        it.flatMap {
            repository.getSpells()
                .observeOn(schedulerProvider.computation())
                .map { data ->
                    LoadResult.Success(data)
                }
                .cast(LoadResult::class.java)
                .onErrorReturn { error ->
                    LoadResult.Error(error)
                }
                .startWithItem(LoadResult.Loading)
        }
    }
}