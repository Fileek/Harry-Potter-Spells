package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.ext.toSpell
import com.epam.harrypotterspells.features.details.DetailsAction.GetSpellAction
import com.epam.harrypotterspells.features.details.DetailsResult.GetSpellResult
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetSpellUseCase @Inject constructor(
    private val repository: Repository
) {

    fun performAction() = ObservableTransformer<GetSpellAction, GetSpellResult> {
        it.flatMap { action ->
            repository.getSpellById(action.id)
                .observeOn(Schedulers.computation())
                .map { jsonSpell ->
                    GetSpellResult.Success(jsonSpell.toSpell())
                }
        }
    }
}