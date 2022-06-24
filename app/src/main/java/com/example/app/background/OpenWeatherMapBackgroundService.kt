package com.example.app.background

import android.content.Context
import android.util.Log.d
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.app.api.RestClient
import com.example.app.api.dto.weatherdata.WeatherData
import com.example.app.constants.Constants
import com.example.app.notifications.WeatherAppNotificationUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OpenWeatherMapBackgroundService(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    override fun doWork(): Result {
//        val city = inputData.getString("city")!!
        val temp = retrofit()
        val outputData = workDataOf("temp" to temp)
        return Result.success(outputData)
    }

    private fun retrofit(): String? {
        var temp: String? = null
        RestClient.openWeatherMapService.getWeather(
            q = "Tbilisi",
            units = "metric",
            appid = Constants.API_KEY
        ).enqueue(object : Callback<WeatherData> {
            override fun onResponse(
                call: Call<WeatherData>, response: Response<WeatherData>
            ) {
                if (response.isSuccessful) {
                    temp = response.body()?.main?.temp.toString()
                    d("LOG WEATHER", "Weather$temp")
                    Toast.makeText(applicationContext, "Weather$temp℃", Toast.LENGTH_LONG).show()
                    WeatherAppNotificationUtil.showRepeatNotification(applicationContext, temp!!)
                }
            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                throw t
            }
        })
        return temp
    }
}