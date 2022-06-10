package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.repository.local.LocalRepository
import com.epam.harrypotterspells.data.repository.remote.RemoteRepository
import com.epam.harrypotterspells.domain.GetLocalSpellsUseCase
import com.epam.harrypotterspells.domain.GetRemoteSpellsUseCase
import com.epam.harrypotterspells.domain.SaveLocalSpellUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesGetLocalSpellsUseCase(repo: LocalRepository) = GetLocalSpellsUseCase(repo)

    @[ViewModelScoped Provides]
    fun providesGetRemoteSpellsUseCase(repo: RemoteRepository) = GetRemoteSpellsUseCase(repo)

    @[ViewModelScoped Provides]
    fun providesSaveLocalSpellUseCase(repo: LocalRepository) = SaveLocalSpellUseCase(repo)
}