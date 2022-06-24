package com.example.app.receivers

import android.Manifest
import android.content.Context
import android.location.LocationListener
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import android.location.LocationManager
import android.os.Bundle

class GPSLocation(var context: Context) : LocationListener {
    val location: Location?
        get() {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Permission not granted", Toast.LENGTH_LONG).show()
                return null
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (isGPSEnabled) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    6000,
                    10f,
                    this
                )
                return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                Toast.makeText(context, "Please enable GPS! ", Toast.LENGTH_LONG).show()
            }
            return null
        }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}