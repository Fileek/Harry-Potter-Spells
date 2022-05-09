package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.utils.toSpell
import com.epam.harrypotterspells.features.spells.SpellsAction.LoadSpellsAction
import com.epam.harrypotterspells.features.spells.SpellsResult.LoadSpellsResult
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoadSpellsUseCase @Inject constructor(
    private val repository: Repository
) {

    fun performAction() =
        ObservableTransformer<LoadSpellsAction, LoadSpellsResult> {
            it.flatMap {
                repository.getSpells()
                    .observeOn(Schedulers.computation())
                    .map { data ->
                        LoadSpellsResult.Success(data.map { jsonSpell ->
                            jsonSpell.toSpell()
                        })
                    }
                    .cast(LoadSpellsResult::class.java)
                    .onErrorReturn { error ->
                        LoadSpellsResult.Error(error)
                    }
                    .startWithItem(LoadSpellsResult.Loading)
            }
        }
}