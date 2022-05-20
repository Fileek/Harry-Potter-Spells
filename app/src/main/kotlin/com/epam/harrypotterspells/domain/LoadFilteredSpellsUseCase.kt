package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadFilteredAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import com.epam.harrypotterspells.util.extension.filterByStringAndHighlightIt
import io.reactivex.rxjava3.core.ObservableTransformer

abstract class LoadFilteredSpellsUseCase<A : LoadFilteredAction>(
    private val repository: Repository
) : UseCase<A, SpellsResult> {

    override fun performAction() =
        ObservableTransformer<A, SpellsResult> {
            it.flatMap { action ->
                repository.getSpells()
                    .map { data ->
                        SpellsResult.Success(data = data.filterByStringAndHighlightIt(action.filter))
                    }
                    .cast(SpellsResult::class.java)
                    .onErrorReturn { error ->
                        SpellsResult.Error(error)
                    }
                    .toObservable()
                    .startWithItem(SpellsResult.Loading)
            }
        }
}