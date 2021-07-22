package com.aasati.hiltdemoapp.di

import com.aasati.hiltdemoapp.repository.WeatherRepositoryImpl
import com.aasati.hiltdemoapp.usecase.WeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideUseCase(
        weatherRepository: WeatherRepositoryImpl
    ): WeatherUseCase {
        return WeatherUseCase(
            repository = weatherRepository
        )
    }
}
