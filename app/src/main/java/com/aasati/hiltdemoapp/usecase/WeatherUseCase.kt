package com.aasati.hiltdemoapp.usecase

import com.aasati.hiltdemoapp.common.Resource
import com.aasati.hiltdemoapp.extension.toResource
import com.aasati.hiltdemoapp.model.WeatherResponse
import com.aasati.hiltdemoapp.repository.WeatherRepositoryImpl
import javax.inject.Inject

class WeatherUseCase @Inject constructor(private val repository: WeatherRepositoryImpl) :
    BaseUseCase<String, WeatherResponse>() {

    override suspend fun createSuspend(data: String): Resource<WeatherResponse> =
        repository.fetchWeather(data).toResource()
}
