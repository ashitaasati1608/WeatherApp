package com.aasati.hiltdemoapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rain(
    @Json(name = "`1h`") val `1h`: Double = 0.0
)
