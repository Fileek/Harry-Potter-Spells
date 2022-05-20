package com.epam.harrypotterspells.domain

import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.feature.spells.SpellsAction
import javax.inject.Inject

class LoadRemoteFilteredSpellsUseCase @Inject constructor(
    repo: RemoteRepository
) : LoadFilteredSpellsUseCase<SpellsAction.LoadFilteredAction.LoadRemoteAction>(repo)