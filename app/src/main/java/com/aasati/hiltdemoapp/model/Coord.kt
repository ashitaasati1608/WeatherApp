package com.aasati.hiltdemoapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Coord(
    @Json(name = "lat") val lat: Double = 0.0,
    @Json(name = "long") val lon: Double = 0.0
)
