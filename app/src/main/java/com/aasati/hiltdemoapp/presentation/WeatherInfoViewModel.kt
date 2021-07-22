package com.aasati.hiltdemoapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aasati.hiltdemoapp.common.SafeMutableLiveData
import com.aasati.hiltdemoapp.extension.onError
import com.aasati.hiltdemoapp.extension.onSuccess
import com.aasati.hiltdemoapp.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherInfoViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase) :
    ViewModel() {

    private val state = SafeMutableLiveData(WeatherState())
    val isError = state.distinct { it.isError }
    val weatherInfo = state.distinct { it.weatherResponse }
    val progress = state.distinct { s -> s.progress }

    fun fetchWeather(cityName: String) {
        viewModelScope.launch {
            state.update { s -> s.setProgress(true) }
            weatherUseCase(cityName)
                .onSuccess { state.update { s -> s.setWeatherInfo(it) } }
                .onError { state.update { s -> s.setError(true) } }
                .also { state.update { s -> s.setProgress(false) } }
        }
    }
}
