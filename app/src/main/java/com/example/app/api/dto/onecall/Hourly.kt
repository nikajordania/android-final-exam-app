package com.example.app.api.dto.onecall


import com.example.app.api.dto.weatherdata.Weather
import com.google.gson.annotations.SerializedName

data class Hourly(
    @SerializedName("dt")
    val dt: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_deg")
    val windDeg: Double,
    @SerializedName("wind_gust")
    val windGust: Double,
    @SerializedName("weather")
    val weather: List<Weather>,
    val pop: Double,
)