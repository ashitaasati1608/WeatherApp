package com.aasati.hiltdemoapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Main(
    @Json(name = "feels_like") val feels_like: Double = 0.0,
    @Json(name = "humidity") val humidity: Int? = null,
    @Json(name = "pressure") val pressure: Int? = null,
    @Json(name = "temp") val temp: Double = 0.0,
    @Json(name = "temp_max") val temp_max: Double = 0.0,
    @Json(name = "temp_min") val temp_min: Double = 0.0
)
