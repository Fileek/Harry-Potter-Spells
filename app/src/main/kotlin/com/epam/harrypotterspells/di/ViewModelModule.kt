package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.Repository
import com.epam.harrypotterspells.mvibase.MVIProcessorHolder
import com.epam.harrypotterspells.spells.SpellsAction
import com.epam.harrypotterspells.spells.SpellsProcessorHolder
import com.epam.harrypotterspells.spells.SpellsResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@[Module InstallIn(ViewModelComponent::class)]
object ViewModelModule {

    @[ViewModelScoped Provides]
    fun providesProcessorHolder(
        repository: Repository
    ) : MVIProcessorHolder<SpellsAction, SpellsResult> {
        return SpellsProcessorHolder(repository)
    }
}