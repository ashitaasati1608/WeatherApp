package com.aasati.hiltdemoapp.repository

import com.aasati.hiltdemoapp.api.WeatherService
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val weatherService: WeatherService) :
    BaseRepository() {

    suspend fun fetchWeather(cityName: String) =
        execute { weatherService.getWeather(cityName, "9bf58934e828a89d6c61223ccdcd1d4c") }
}
