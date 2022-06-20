package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.api.RestClient
import com.example.app.api.dto.onecall.OneCall
import com.example.app.api.dto.weatherdata.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val cities = listOf(
            "Tbilisi",
            "Batumi",
            "Kutaisi",
            "Rustavi",
            "London",
            "Hawaii",
//            "Gori",
//            "Zugdidi",
//            "Poti",
//            "Kobuleti",
//            "Khasuri",
//            "Samtredia"
        )

        recyclerView = findViewById(R.id.recyclerView)

        val x = mutableListOf<WeatherData>()

        RestClient.initClient()
        for (city in cities) {
            RestClient.openWeatherMapService.getWeather(
                q = city,
                units = "metric",
                appid = "03006558897f1413ecaaf87f7cadec03"
            ).enqueue(object : Callback<WeatherData> {
                override fun onResponse(
                    call: Call<WeatherData>, response: Response<WeatherData>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { x.add(it) }
                        Log.d("Successful: " + city, response.body().toString())
                    }
                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
        }


//        val oneCall: OneCall? = RestClient.openWeatherMapService.getOneCall(
//            lat = 33.44,
//            lon = -94.04,
//            exclude = "hourly,daily",
//            units = "metric",
//            appid = "03006558897f1413ecaaf87f7cadec03"
//        ).execute().body()
//
//        Log.d("Onecall", oneCall.toString())

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = WeatherRecyclerAdapter(x)
        }
    }
}