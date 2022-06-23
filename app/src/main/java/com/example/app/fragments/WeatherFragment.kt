package com.example.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.WeatherRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private lateinit var recyclerView: RecyclerView
    private val cities = listOf(
        "Tbilisi",
        "Batumi",
        "Kutaisi",
        "Rustavi",
        "London",
        "Hawaii"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)

        val weatherListData = mutableListOf<WeatherData>()

        RestClient.initClient()
        for (city in cities) {
            RestClient.openWeatherMapService.getWeather(
                q = city,
                units = "metric",
                appid = Constants.API_KEY
            ).enqueue(object : Callback<WeatherData> {
                override fun onResponse(
                    call: Call<WeatherData>, response: Response<WeatherData>
                ) {
                    if (response.isSuccessful) {
                        weatherListData.add(response.body()!!)
                        showData(weatherListData)
                        Log.d("Successful: $city", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
        }
    }

    private fun showData(weatherListData: MutableList<WeatherData>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = WeatherRecyclerAdapter(weatherListData)
        }
    }
}