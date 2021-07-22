package com.aasati.hiltdemoapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.aasati.hiltdemoapp.R
import com.aasati.hiltdemoapp.common.SafeObserver
import com.aasati.hiltdemoapp.extension.gone
import com.aasati.hiltdemoapp.extension.hideKeyboard
import com.aasati.hiltdemoapp.extension.setVisible
import com.aasati.hiltdemoapp.extension.show
import com.aasati.hiltdemoapp.model.WeatherResponse
import com.aasati.hiltdemoapp.presentation.WeatherInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.view_weather_info.*
import kotlinx.android.synthetic.main.weather_info_fragment.*


@AndroidEntryPoint
class WeatherInfoFragment : Fragment() {

    private val weatherViewModel: WeatherInfoViewModel by activityViewModels()

    private var cityName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.weather_info_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObservers()
        setOnClickListener()
    }

    private fun initView() {
        //initialise views
        et_enter_city.addTextChangedListener(textWatcher)
    }

    private fun initObservers() {
        weatherViewModel.progress.observe(
            viewLifecycleOwner, SafeObserver {
                //set progress
                progress_bar.setVisible(it)
            }
        )

        weatherViewModel.weatherInfo.observe(
            viewLifecycleOwner, { weatherInfo ->
                weatherInfo?.let {
                    //set weather response
                    layout_weather_info.show()
                    layout_view_error.gone()
                    setWeatherInfo(weatherInfo)
                }
            }
        )

        weatherViewModel.isError.observe(
            viewLifecycleOwner, SafeObserver { showError ->
                //set Error view
                if (showError) {
                    layout_weather_info.gone()
                    layout_view_error.show()
                }
            }
        )
    }

    private fun setWeatherInfo(weatherInfo: WeatherResponse) {
        weatherInfo.apply {
            tv_city_name.text = name
            tv_city_country.text = resources.getString(R.string.country_name, sys?.country)
            tv_temp.text = resources.getString(R.string.temp, main?.temp.toString())
            tv_feels_like.text =
                resources.getString(R.string.feels_like, main?.feels_like.toString())
            tv_humidity.text = resources.getString(R.string.humidity, main?.humidity.toString())

            weather.let {
                tv_weather.text =
                    resources.getString(R.string.weather_condition, it.firstOrNull()?.main)
                tv_weather_desc.text =
                    resources.getString(R.string.weather_desc, it.firstOrNull()?.description)
            }
        }
    }

    private fun setOnClickListener() {
        btn_fetch_weather.setOnClickListener { fetchWeatherInfo(it) }

        layout_view_error.setOnClickListener { fetchWeatherInfo(it) }
    }

    private fun fetchWeatherInfo(view: View) {
        view.hideKeyboard()
        if (!cityName.isNullOrEmpty()) {
            layout_view_error.gone()
            weatherViewModel.fetchWeather(cityName!!)
        } else {
            Toast.makeText(context, "City name can't  be empty", Toast.LENGTH_LONG).show()
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            cityName = et_enter_city.text.toString()
        }

        override fun afterTextChanged(s: Editable) {}
    }
}
