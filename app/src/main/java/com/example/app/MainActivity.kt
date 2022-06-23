package com.example.app

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
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
}