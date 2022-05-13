package com.epam.harrypotterspells.di

import com.epam.harrypotterspells.data.repository.Repository
import com.epam.harrypotterspells.data.api.SpellApi
import com.epam.harrypotterspells.data.repository.DefaultRepository
import com.epam.harrypotterspells.util.scheduler.DefaultSchedulerProvider
import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@[Module InstallIn(SingletonComponent::class)]
object AppModule {

    private const val BASE_URL = "https://wizard-world-api.herokuapp.com"

    @[Singleton Provides]
    fun providesApi(): SpellApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(SpellApi::class.java)
    }

    @[Singleton Provides]
    fun providesRepository(
        api: SpellApi,
        schedulerProvider: SchedulerProvider
    ): Repository {
        return DefaultRepository(api, schedulerProvider)
    }

    @[Singleton Provides]
    fun providesSchedulerProvider(): SchedulerProvider = DefaultSchedulerProvider()
}