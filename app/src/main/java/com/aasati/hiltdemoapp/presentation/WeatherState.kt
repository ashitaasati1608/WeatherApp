package com.aasati.hiltdemoapp.presentation

import com.aasati.hiltdemoapp.model.WeatherResponse

data class WeatherState(
    val cityName: String? = null,
    val weatherResponse: WeatherResponse? = null,
    val isError: Boolean = false,
    val progress: Boolean = false
) {

    fun setWeatherInfo(weatherResponse: WeatherResponse) = copy(weatherResponse = weatherResponse)

    fun setError(isError: Boolean) = copy(isError = isError)

    fun setProgress(isLoading: Boolean) = copy(progress = isLoading)
}
