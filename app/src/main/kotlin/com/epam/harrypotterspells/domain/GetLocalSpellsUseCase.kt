package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction.GetLocalAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LocalResult
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

/**
 * [UseCase] to retrieve data from [LocalRepository].
 * @param repository local data source.
 */
class GetLocalSpellsUseCase @Inject constructor(
    private val repository: LocalRepository
) : UseCase {

    fun performAction() = ObservableTransformer<GetLocalAction, LocalResult> {
        it.flatMap {
            repository.getSpells()
                .map { data ->
                    val spells = data.map { jsonSpell ->  jsonSpell.toSpell() }
                    LocalResult(spells)
                }
                .toObservable()
        }
    }
}