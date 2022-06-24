package com.example.app.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.app.R

object WeatherAppNotificationUtil {
    private const val CHANNEL_ID = "WEATHERAPPNOTIFICATIONCHANNEL"
    private lateinit var manager: NotificationManager

    fun showAirplaneModeNotification(context: Context, status: Boolean) {
        if (status) {
            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_airplanemode_active_24)
                .setContentTitle("Weather App: Airplane mode is active")
                .setContentText("Please disable airplane mode!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = CHANNEL_ID
                val descriptionText = "AirplaneMode"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    name,
                    descriptionText,
                    importance
                ).apply {
                    description = descriptionText
                }

                manager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
                manager.notify(1, notification.build())

            }
        } else {
            manager.cancel(1)
        }
    }

    fun showRepeatNotification(context: Context, temp: String) {

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_airplanemode_active_24)
            .setContentTitle("Weather App: Current Weather")
            .setContentText("Current Weather: $temp℃")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = "WeatherApp"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                name,
                descriptionText,
                importance
            ).apply {
                description = descriptionText
            }

            manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            manager.notify(2, notification.build())

        }
    }
}
