package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.entity.Spell
import com.epam.harrypotterspells.feature.details.DetailsResult.SaveSpellFieldResult
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import io.reactivex.rxjava3.core.ObservableTransformer
import javax.inject.Inject

/**
 * UseCase to save changed spell in [LocalRepository].
 * @param localRepository local data source to update and retrieve data.
 */
class SaveLocalSpellUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) : UseCase {

    fun performAction(spell: Spell): SpellsResult.LocalResult {
        localRepository.saveSpell(spell.toJsonSpell())
        val spells =
            localRepository.getSpells()
                .blockingGet()
                .map { it.toSpell() }
        return SpellsResult.LocalResult(spells)
    }
}