package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.GetSpellUseCase
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
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
    ): LoadSpellsUseCase {
        return LoadSpellsUseCase(repository)
    }

    @[ViewModelScoped Provides]
    fun providesGetSpellUseCase(
        repository: Repository
    ): GetSpellUseCase {
        return GetSpellUseCase(repository)
    }

    @[ViewModelScoped Provides]
    fun providesEditSpellUseCase(): EditSpellUseCase {
        return EditSpellUseCase()
    }

    @[ViewModelScoped Provides]
    fun providesUpdateSpellUseCase(
        repository: Repository
    ): UpdateSpellUseCase {
        return UpdateSpellUseCase(repository)
    }
}