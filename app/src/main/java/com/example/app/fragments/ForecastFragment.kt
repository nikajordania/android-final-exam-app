package com.example.app.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationRequest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.adapters.ForecastRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.onecall.OneCall
import com.example.app.constants.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ForecastFragment : Fragment(R.layout.fragment_forecast) {
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

        getLocation()

        if(lat != null && lon != null){
            getData(lat!!, lon!!)
        }
    }

    fun getLocation(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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