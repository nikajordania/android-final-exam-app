package com.example.app.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient


    fun initClient() {
        okHttpClient = OkHttpClient.Builder().build()
        retrofit = Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/").addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    }

    private fun <S> getService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }

    val openWeatherMapService: OpenWeatherMapService get() = getService(OpenWeatherMapService::class.java)
}