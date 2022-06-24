package com.example.app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.example.app.background.OpenWeatherMapBackgroundService
import com.example.app.notifications.WeatherAppNotificationUtil.showRepeatNotification
import com.example.app.receivers.AirplaneModeReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var navController: NavController
    private val workManager = WorkManager.getInstance(this)

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            123
        )

        val airplaneModeReceiver = AirplaneModeReceiver()

        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(airplaneModeReceiver, it)
        }

        val c = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED).build()
//        val city = "Tbilisi"
//        val inputData = Data.Builder().putString("city", city).build()

        val weatherBackService = PeriodicWorkRequestBuilder<OpenWeatherMapBackgroundService>(
            PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
            TimeUnit.MINUTES
        )
            .setConstraints(c)
//            .setInputData(inputData)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "weather",
            ExistingPeriodicWorkPolicy.REPLACE,
            weatherBackService
        )

        workManager.getWorkInfoByIdLiveData(weatherBackService.id)
            .observe(this, Observer { workInfo ->
                if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val temp = workInfo.outputData.getString("temp")!!
                    Toast.makeText(applicationContext, "Weather$temp", Toast.LENGTH_LONG).show()
                    showRepeatNotification(applicationContext, temp)
                }
            })


        bottomNavigationView = findViewById(R.id.bottomNavView)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

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
}