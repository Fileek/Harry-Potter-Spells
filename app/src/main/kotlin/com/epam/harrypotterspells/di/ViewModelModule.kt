package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.domain.EditUseCase
import com.epam.harrypotterspells.domain.LoadSpellsUseCase
import com.epam.harrypotterspells.domain.SearchUseCase
import com.epam.harrypotterspells.domain.SwitchSourceUseCase
import com.epam.harrypotterspells.domain.UpdateSpellUseCase
import com.epam.harrypotterspells.domain.UseCase
import com.epam.harrypotterspells.feature.details.DetailsAction.EditAction
import com.epam.harrypotterspells.feature.details.DetailsAction.UpdateAction
import com.epam.harrypotterspells.feature.details.DetailsResult.EditResult
import com.epam.harrypotterspells.feature.details.DetailsResult.UpdateResult
import com.epam.harrypotterspells.feature.spells.SpellsAction.LoadAction
import com.epam.harrypotterspells.feature.spells.SpellsResult.LoadResult
import com.epam.harrypotterspells.feature.main.MainAction
import com.epam.harrypotterspells.feature.main.MainResult
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
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
    ): UseCase<LoadAction, LoadResult> {
        return LoadSpellsUseCase(repo, schedulerProvider)
    }

    @[ViewModelScoped Provides]
    fun providesSearchUseCase(
        repo: Repository
    ): UseCase<MainAction.SearchAction, MainResult.SearchResult> {
        return SearchUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesEditSpellUseCase(): UseCase<EditAction, EditResult> {
        return EditUseCase()
    }

    @[ViewModelScoped Provides]
    fun providesUpdateSpellUseCase(
        repo: Repository
    ): UseCase<UpdateAction, UpdateResult> {
        return UpdateSpellUseCase(repo)
    }

    @[ViewModelScoped Provides]
    fun providesSwitchSourceUseCase(
        repo: Repository
    ): UseCase<MainAction.SwitchSourceAction, MainResult.SwitchSourceResult> {
        return SwitchSourceUseCase(repo)
    }
}