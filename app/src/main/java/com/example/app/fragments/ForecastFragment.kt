package com.example.app.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.ForecastRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.onecall.Current
import com.example.app.api.dto.onecall.Hourly
import com.example.app.api.dto.onecall.OneCall
import com.example.app.api.dto.weatherdata.Main
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants
import com.example.app.database.App
import com.example.app.database.Forecast
import com.example.app.database.ForecastDao
import com.example.app.database.Weather
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    lateinit var dao: ForecastDao
    lateinit var preference: SharedPreferences
    lateinit var recyclerView: RecyclerView
    lateinit var imageView: ImageView
    lateinit var cityText: TextView
    lateinit var weatherText: TextView
    lateinit var humidityText: TextView
    var lat : Double? = null
    var lon : Double? = null
    lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.weatherIcon)
        cityText = view.findViewById(R.id.city)
        weatherText = view.findViewById(R.id.weather)
        humidityText = view.findViewById(R.id.humidity)
        recyclerView = view.findViewById(R.id.recyclerView)
        preference = requireActivity().getSharedPreferences("preferences_forecast", Context.MODE_PRIVATE)

        App(requireContext())
        if(App.instance != null) dao = App.instance!!.db.forecastDao()

        getLocation()
    }

    fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
            return
        }

        val location = fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_BALANCED_POWER_ACCURACY, null)

        location.addOnCompleteListener { location ->
            lat = location.result.latitude
            lon = location.result.longitude

            getData(lat!!, lon!!)
        }
    }

    fun getData(lat: Double, lon: Double) {
        RestClient.initClient()

        val new_time = Calendar.getInstance().timeInMillis
        val old_time = preference.getLong("time", 0L)
        var oneCall: OneCall?

        if ((new_time - old_time) >= 300000) {
            Toast.makeText(context, "Loading from API", Toast.LENGTH_SHORT).show()

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
                        oneCall = response.body()

                        showData(oneCall)
                        saveData(oneCall!!)
                    }
                }

                override fun onFailure(call: Call<OneCall>, t: Throwable) {
                    throw t
                }
            })
        } else{
            Toast.makeText(context, "Loading from DB", Toast.LENGTH_SHORT).show()
            loadFromDatabase()
        }
    }

    private fun saveData(oneCall: OneCall){
        val time = Calendar.getInstance().timeInMillis
        preference.edit().apply {
            putLong("time", time)
            putString("name", oneCall.timezone)
            putFloat("temp", oneCall.current!!.temp.toFloat())
            putInt("humidity", oneCall.current.humidity)
            putString("icon", oneCall.current.weather[0].icon)
        }.apply()

        clearDatabase()
        saveToDatabase(oneCall)

    }

    @SuppressLint("SetTextI18n")
    fun showData(oneCall: OneCall?){
        Picasso.get().load(getIcon(oneCall!!.current!!.weather[0].icon)).into(imageView)
        cityText.text = oneCall.timezone
        weatherText.text = oneCall.current!!.temp.toInt().toString() + "℃"
        humidityText.text = oneCall.current.humidity.toString() + "%"

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ForecastRecyclerAdapter(oneCall)
        }
    }

    private fun getIcon(id: String): String {
        return "https://openweathermap.org/img/wn/$id@4x.png"
    }

    private fun clearDatabase(){
        dao.resetRecords()
    }

    private fun saveToDatabase(oneCall: OneCall){
        val hourlyListFromApi = oneCall.hourly
        var forecast: Forecast

        hourlyListFromApi.forEach{ data ->
            forecast = Forecast(
                0,
                data.temp,
                data.humidity,
                data.dt,
                data.weather[0].icon
            )

            dao.saveRecord(forecast)
        }
    }

    private fun loadFromDatabase(){
        val dataList = dao.getRecords()
        var weatherList: List<com.example.app.api.dto.onecall.Weather>
        val hourlyList: MutableList<Hourly> = mutableListOf()
        val oneCall: OneCall

        dataList.forEach{ record ->
            weatherList = listOf(com.example.app.api.dto.onecall.Weather(
                null, record.icon, null, null
            ))

            hourlyList.add(
                Hourly(
                    record.time, record.temp,
                    null, null,
                    record.humidity,
                    null, null, null, null, null, null, null,
                    weatherList, null
                ))
        }

        val currentWeatherList: List<com.example.app.api.dto.onecall.Weather> = listOf(
            com.example.app.api.dto.onecall.Weather(
                null, preference.getString("icon", "")!!, null, null
            )
        )

        oneCall = OneCall(
            Current(
                null, null,
                preference.getLong("time", 0).toInt(),
                null,
                preference.getInt("humidity", 0),
                null, null, null,
                preference.getFloat("temp", 0.0f).toDouble(),
                null, null,
                currentWeatherList,
                null, null, null
            ),
            null, null, hourlyList, preference.getString("name", "null"), null
        )

        showData(oneCall)
    }
}