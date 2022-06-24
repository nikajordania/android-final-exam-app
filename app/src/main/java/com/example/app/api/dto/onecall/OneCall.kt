package com.example.app.api.dto.onecall


import com.google.gson.annotations.SerializedName

data class OneCall(
    @SerializedName("current")
    val current: Current?,
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lon")
    val lon: Double?,
    @SerializedName("hourly")
    val hourly: List<Hourly>,
    @SerializedName("timezone")
    val timezone: String?,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int?
)