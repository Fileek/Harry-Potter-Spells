package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.features.details.DetailsReducer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesLoadSpellsUseCase(repo: Repository) = LoadSpellsUseCase(repo)

    @[ViewModelScoped Provides]
    fun providesEditSpellUseCase() = EditSpellUseCase()

    @[ViewModelScoped Provides]
    fun providesUpdateSpellUseCase(repo: Repository) = UpdateSpellUseCase(repo)

    @[ViewModelScoped Provides]
    fun providesDetailsReducer() = DetailsReducer()
}