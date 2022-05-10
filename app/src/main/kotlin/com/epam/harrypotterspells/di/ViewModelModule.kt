package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.domain.EditSpellUseCase
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.domain.SwitchToLocalUseCase
import com.epam.harrypotterspells.domain.SwitchToRemoteUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.features.details.DetailsAction.EditSpellAction
import com.epam.harrypotterspells.features.details.DetailsAction.UpdateSpellAction
import com.epam.harrypotterspells.features.details.DetailsReducer
import com.epam.harrypotterspells.features.details.DetailsResult.EditSpellResult
import com.epam.harrypotterspells.features.details.DetailsResult.UpdateSpellResult
import com.epam.harrypotterspells.features.spells.SpellsAction.LoadSpellsAction
import com.epam.harrypotterspells.features.spells.SpellsResult.LoadSpellsResult
import com.epam.harrypotterspells.main.MainAction.SwitchToLocalAction
import com.epam.harrypotterspells.main.MainAction.SwitchToRemoteAction
import com.epam.harrypotterspells.main.MainResult.SwitchToLocalResult
import com.epam.harrypotterspells.main.MainResult.SwitchToRemoteResult
import com.epam.harrypotterspells.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesLoadSpellsUseCase(
        repo: Repository,
        schedulerProvider: SchedulerProvider
    ): UseCase<LoadSpellsAction, LoadSpellsResult> {
        return LoadSpellsUseCase(repo, schedulerProvider)
    }

    @[ViewModelScoped Provides]
    fun providesEditSpellUseCase(): UseCase<EditSpellAction, EditSpellResult> {
        return EditSpellUseCase()
    }

    @[ViewModelScoped Provides]
    fun providesUpdateSpellUseCase(
        repo: Repository
    ): UseCase<UpdateSpellAction, UpdateSpellResult> {
        return UpdateSpellUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesSwitchToLocalUseCase(
        repo: Repository
    ): UseCase<SwitchToLocalAction, SwitchToLocalResult> {
        return SwitchToLocalUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesSwitchToRemoteUseCase(
        repo: Repository
    ): UseCase<SwitchToRemoteAction, SwitchToRemoteResult> {
        return SwitchToRemoteUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesDetailsReducer() = DetailsReducer()
}