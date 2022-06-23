package com.example.app

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.adapters.WeatherRecyclerAdapter
import com.example.app.api.RestClient
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants.API_KEY
import com.example.app.receivers.GPSLocation
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
//    private var locationManager: LocationManager? = null
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            123
        )

        bottomNavigationView = findViewById(R.id.bottomNavView)
        navController = findNavController(R.id.navHostFragment)

        setupActionBarWithNavController(navController, AppBarConfiguration(
            setOf(
                R.id.weatherFragment,
                R.id.forecastFragment,
                R.id.premiumFragment
            )
        )
        )

        bottomNavigationView.setupWithNavController(navController)
    }
//        val g = GPSLocation(applicationContext)
//
//        val l: Location = g.location!!
//
//        if (l != null) {
//            val lat = l.latitude
//
//            val lon = l.longitude
//
//            Toast.makeText(applicationContext, "latitude: $lat\nlongitude: $lon", Toast.LENGTH_LONG)
//                .show()


//            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
//
//        locationManager?.requestLocationUpdates(
//            LocationManager.NETWORK_PROVIDER,
//            0L,
//            0f,
//            locationListener
//        )
//            val cities = listOf(
//                "Tbilisi",
//                "Batumi",
//                "Kutaisi",
//                "Rustavi",
//                "London",
//                "Hawaii",
////            "Gori",
////            "Zugdidi",
////            "Poti",
////            "Kobuleti",
////            "Khasuri",
////            "Samtredia"
//            )

//            recyclerView = findViewById(R.id.recyclerView)

//            val weatherListData = mutableListOf<WeatherData>()
//
//            RestClient.initClient()
//            for (city in cities) {
//                RestClient.openWeatherMapService.getWeather(
//                    q = city,
//                    units = "metric",
//                    appid = API_KEY
//                ).enqueue(object : Callback<WeatherData> {
//                    override fun onResponse(
//                        call: Call<WeatherData>, response: Response<WeatherData>
//                    ) {
//                        if (response.isSuccessful) {
//                            weatherListData.add(response.body()!!)
//                            showData(weatherListData)
//                            Log.d("Successful: $city", response.body().toString())
//                        }
//                    }
//
//                    override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//                        Log.d("Failure", t.message.toString())
//                    }
//                })
//            }


//        val oneCall: OneCall? = RestClient.openWeatherMapService.getOneCall(
//            lat = 33.44,
//            lon = -94.04,
//            exclude = "minutely,daily",
//            units = "metric",
//            appid = API_KEY
//        ).execute().body()
//
//        Log.d("Onecall", oneCall.toString())


//        }
//    }

//    private fun showData(weatherListData: MutableList<WeatherData>) {
//        recyclerView.apply {
//            layoutManager = LinearLayoutManager(this@MainActivity)
//            adapter = WeatherRecyclerAdapter(weatherListData)
//        }
//    }

//    private val locationListener: LocationListener = object : LocationListener {
//        @SuppressLint("ShowToast")
//        override fun onLocationChanged(location: Location) {
//            Toast.makeText(
//                applicationContext,
//                "lon: ${location.longitude}, lat: ${location.latitude}",
//                Toast.LENGTH_SHORT
//            )
//        }
//
//    }
}