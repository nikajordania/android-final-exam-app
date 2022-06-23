package com.example.app.fragments

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.ForecastRecyclerAdapter
import com.example.app.adapters.WeatherRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.onecall.Hourly
import com.example.app.api.dto.onecall.OneCall
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants
import com.example.app.receivers.GPSLocation
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    lateinit var recyclerView: RecyclerView
    lateinit var coordinates : List<Double>
    lateinit var imageView: ImageView
    lateinit var cityText: TextView
    lateinit var weatherText: TextView
    lateinit var humidityText: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.weatherIcon)
        cityText = view.findViewById(R.id.city)
        weatherText = view.findViewById(R.id.weather)
        humidityText = view.findViewById(R.id.humidity)

        recyclerView = view.findViewById(R.id.recyclerView)
        coordinates = getLocation()

        getData(coordinates[1], coordinates[0])
    }

    fun getLocation() : List<Double>{
        val gps = GPSLocation(requireContext())

        val loc: Location = gps.location!!

        if(loc != null) return listOf(loc.longitude, loc.latitude)
        else throw Exception("Error fetching location.")
    }

    fun getData(lat: Double, lon: Double){
        RestClient.initClient()
        RestClient.openWeatherMapService.getOneCall(
            lat = lat,
            lon = lon,
            exclude = "minutely,daily",
            units = "metric",
            appid = Constants.API_KEY
        ).enqueue(object : Callback<OneCall> {
            override fun onResponse(
                call: Call<OneCall>, response: Response<OneCall>
            ) {
                if (response.isSuccessful) {
                    showData(response.body())
                }
            }

            override fun onFailure(call: Call<OneCall>, t: Throwable) {
                throw t
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun showData(data: OneCall?){
        Picasso.get().load(getIcon(data!!.current.weather[0].icon)).into(imageView)
        cityText.text = data.timezone
        weatherText.text = data.current.temp.toInt().toString() + "℃"
        humidityText.text = data.current.humidity.toString() + "%"

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ForecastRecyclerAdapter(data)
        }
    }

    private fun getIcon(id: String): String {
        return "https://openweathermap.org/img/wn/$id@4x.png"
    }
}