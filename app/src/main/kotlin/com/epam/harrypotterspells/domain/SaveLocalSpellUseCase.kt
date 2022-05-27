package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction.SaveSpellAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LocalResult
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * UseCase to save changed spell in [LocalRepository].
 * @param repository local data source to update and retrieve data.
 */
class SaveLocalSpellUseCase @Inject constructor(
    private val repository: LocalRepository,
) : UseCase {

    fun performAction(action: SaveSpellAction): Observable<LocalResult> {
        repository.saveSpell(action.spell.toJsonSpell())
        return repository.getSpells()
            .map { data ->
                LocalResult(data.map { it.toSpell() })
            }
            .toObservable()
    }
}