package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction
import javax.inject.Inject

class LoadLocalFilteredSpellsUseCase @Inject constructor(
    repo: LocalRepository
) : LoadFilteredSpellsUseCase<SpellsAction.LoadFilteredAction.LoadLocalAction>(repo)