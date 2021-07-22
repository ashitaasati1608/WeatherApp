package com.aasati.hiltdemoapp.di

import com.aasati.hiltdemoapp.api.WeatherService
import com.aasati.hiltdemoapp.repository.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        weatherService: WeatherService
    ): WeatherRepositoryImpl {
        return WeatherRepositoryImpl(
            weatherService = weatherService
        )
    }
}
