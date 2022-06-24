package com.example.app.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.WeatherRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.weatherdata.Main
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants
import com.example.app.database.App
import com.example.app.database.Weather
import com.example.app.database.WeatherDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private lateinit var dao: WeatherDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var preference: SharedPreferences
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
        preference = requireActivity().getSharedPreferences("preferences", MODE_PRIVATE)

        App(requireContext())
        if(App.instance != null) dao = App.instance!!.db.weatherDao()

        getData()
    }

    private fun getData(){
        RestClient.initClient()

        val new_time = Calendar.getInstance().timeInMillis
        val old_time = preference.getLong("time", 0L)
        val weatherListData = mutableListOf<WeatherData>()

        if((new_time - old_time) >= 300000){
            Toast.makeText(context, "Loading from API", Toast.LENGTH_SHORT).show()
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
                        }
                        if(weatherListData.size == cities.size){
                            showData(weatherListData)
                            saveData(weatherListData)
                        }
                    }

                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                        throw t
                    }
                })
            }

        } else{
            Toast.makeText(context, "Loading from DB", Toast.LENGTH_SHORT).show()
            loadFromDatabase(weatherListData)
        }
    }

    private fun saveData(list: MutableList<WeatherData>){
        val time = Calendar.getInstance().timeInMillis
        preference.edit().putLong("time", time).apply()

        clearDatabase()
        saveToDatabase(list)
    }

    private fun saveToDatabase(list: MutableList<WeatherData>){
        var weather: Weather
        var weatherList: List<com.example.app.api.dto.weatherdata.Weather>?

        list.forEach{ data ->
            weatherList = data.weather
            weather = Weather(
                0,
                data.name,
                data.main.temp,
                data.main.humidity,
                data.dt,
                data.timezone,
                weatherList!![0].icon
            )

            dao.saveRecord(weather)
        }
    }

    private fun clearDatabase(){
        dao.resetRecords()
    }

    private fun loadFromDatabase(list: MutableList<WeatherData>){
        val dataList = dao.getRecords()
        var weatherList: List<com.example.app.api.dto.weatherdata.Weather>

        dataList.forEach{ record ->
            weatherList = listOf(com.example.app.api.dto.weatherdata.Weather(
                null, record.icon, null, null
            ))

            list.add(WeatherData(
                null, null, null, null, record.time, null,
                Main(
                    null, record.humidity, null, record.temp, null, null
                ),
                record.name, null, record.timezone, null, weatherList, null
            ))
        }

        showData(list)
    }

    private fun showData(weatherListData: MutableList<WeatherData>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = WeatherRecyclerAdapter(weatherListData)
        }
    }
}