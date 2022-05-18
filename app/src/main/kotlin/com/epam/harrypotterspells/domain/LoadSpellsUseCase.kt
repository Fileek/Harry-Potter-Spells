package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LoadResult
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

class LoadSpellsUseCase @Inject constructor(
    private val repository: Repository,
) : UseCase<LoadAction, LoadResult> {

    override fun performAction() = ObservableTransformer<LoadAction, LoadResult> {
        it.flatMap {
            repository.getSpells()
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