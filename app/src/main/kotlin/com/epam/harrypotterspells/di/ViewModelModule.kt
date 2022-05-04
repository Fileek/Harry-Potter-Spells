package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesLoadSpellsUseCase(
        repository: Repository
    ) : LoadSpellsUseCase {
        return LoadSpellsUseCase(repository)
    }
}