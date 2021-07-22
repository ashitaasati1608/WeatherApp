package com.aasati.hiltdemoapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "deg") val deg: Int? = null,
    @Json(name = "speed") val speed: Double = 0.0
)
