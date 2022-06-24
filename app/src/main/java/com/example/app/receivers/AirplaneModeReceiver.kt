package com.example.app.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.app.notifications.WeatherAppNotificationUtil.showAirplaneModeNotification

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getBooleanExtra("state", false) ?: return
        showAirplaneModeNotification(context!!, state)
    }
}
