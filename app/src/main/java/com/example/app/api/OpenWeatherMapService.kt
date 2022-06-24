package com.example.app.api

import com.example.app.api.dto.onecall.OneCall
import com.example.app.api.dto.weatherdata.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    fun getWeather(
        @Query("q") q: String,
        @Query("units") units: String,
        @Query("APPID") appid: String
    ): Call<WeatherData>

    @GET("/data/2.5/onecall")
    fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("units") units: String,
        @Query("appid") appid: String
    ): Call<OneCall>

}
