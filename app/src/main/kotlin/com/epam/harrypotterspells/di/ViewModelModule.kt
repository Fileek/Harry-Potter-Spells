package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.domain.LoadLocalFilteredSpellsUseCase
import com.epam.harrypotterspells.domain.LoadRemoteFilteredSpellsUseCase
import com.epam.harrypotterspells.domain.SaveSpellUseCase
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.feature.details.DetailsAction
import com.epam.harrypotterspells.feature.details.DetailsResult
import com.epam.harrypotterspells.feature.spells.SpellsAction
import com.epam.harrypotterspells.feature.spells.SpellsResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesSaveSpellUseCase(
        localRepository: LocalRepository,
        remoteRepository: RemoteRepository
    ): UseCase<DetailsAction.SaveSpellAction, DetailsResult.SaveSpellFieldResult> {
        return SaveSpellUseCase(localRepository, remoteRepository)
    }

    @[ViewModelScoped Provides]
    fun providesGetFilteredRemoteSpellsUseCase(
        repo: RemoteRepository
    ): UseCase<SpellsAction.LoadFilteredAction.LoadRemoteAction, SpellsResult> {
        return LoadRemoteFilteredSpellsUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesGetFilteredLocalSpellsUseCase(
        repo: LocalRepository
    ): UseCase<SpellsAction.LoadFilteredAction.LoadLocalAction, SpellsResult> {
        return LoadLocalFilteredSpellsUseCase(repo)
    }
}