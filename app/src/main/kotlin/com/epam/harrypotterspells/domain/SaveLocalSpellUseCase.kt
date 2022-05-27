package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.spells.SpellsResult
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

/**
 * UseCase to save changed spell in [LocalRepository].
 * @param localRepository local data source to update and retrieve data.
 */
class SaveLocalSpellUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) : UseCase {

    fun performAction(spell: Spell): Observable<SpellsResult.LocalResult> {
        localRepository.saveSpell(spell.toJsonSpell())
        return localRepository.getSpells()
            .map { data ->
                SpellsResult.LocalResult(data.map { it.toSpell() })
            }
            .toObservable()
    }
}