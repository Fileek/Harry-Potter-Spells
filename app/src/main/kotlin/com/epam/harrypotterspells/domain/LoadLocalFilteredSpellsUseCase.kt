package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction
import javax.inject.Inject

/**
 * Local realisation of [LoadFilteredSpellsUseCase] that load and filter spells from given [repository].
 * @param repo data source for spells.
 */
class LoadLocalFilteredSpellsUseCase @Inject constructor(
    repo: LocalRepository
) : LoadFilteredSpellsUseCase<SpellsAction.LoadFilteredAction.LoadLocalAction>(repo)