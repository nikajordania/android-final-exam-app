package com.example.app.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.api.dto.onecall.Hourly
import com.example.app.api.dto.onecall.OneCall
import com.example.app.api.dto.weatherdata.WeatherData
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class ForecastRecyclerAdapter(private val weatherListData: OneCall) :
    RecyclerView.Adapter<ForecastRecyclerAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val weatherView: TextView = itemView.findViewById(R.id.weather)
        private val timeView: TextView = itemView.findViewById(R.id.time)
        private val humidity: TextView = itemView.findViewById(R.id.humidity)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        private val frameLayout: FrameLayout = itemView.findViewById(R.id.weatherFrame)
        private var calendar = Calendar.getInstance()
        private var dateFormat = SimpleDateFormat("HH:mm")


        @SuppressLint("SetTextI18n")
        fun onBind(hourly: Hourly) {
            calendar.timeInMillis = (hourly.dt * 1000).toLong()
            val hourDay = calendar.get(Calendar.HOUR_OF_DAY)

            Picasso.get().load(getIcon(hourly.weather[0].icon)).into(weatherIcon)
            weatherView.text = hourly.temp.toInt().toString() + "℃"
            humidity.text = hourly.humidity.toString() + "%"
            timeView.text = dateFormat.format(calendar.timeInMillis).toString()

            if(hourDay in 6..14){
                constraintLayout.setBackgroundColor(Color.parseColor("#E0FEFF"))
                frameLayout.setBackgroundColor(Color.parseColor("#c3eff1"))
                weatherView.setTextColor(Color.parseColor("#000000"))
                humidity.setTextColor(Color.parseColor("#000000"))
                timeView.setTextColor(Color.parseColor("#000000"))

            } else if(hourDay in 15..19){
                constraintLayout.setBackgroundColor(Color.parseColor("#F7B511"))
                frameLayout.setBackgroundColor(Color.parseColor("#ffe29b"))
                weatherView.setTextColor(Color.parseColor("#FFFFFF"))
                humidity.setTextColor(Color.parseColor("#FFFFFF"))
                timeView.setTextColor(Color.parseColor("#FFFFFF"))
            } else{
                constraintLayout.setBackgroundColor(Color.parseColor("#182078"))
                frameLayout.setBackgroundColor(Color.parseColor("#6a72c7"))
                weatherView.setTextColor(Color.parseColor("#FFFFFF"))
                humidity.setTextColor(Color.parseColor("#FFFFFF"))
                timeView.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        private fun getIcon(id: String): String {
            return "https://openweathermap.org/img/wn/$id@4x.png"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_forecast_recycler, parent, false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(weatherListData.hourly[position])
    }

    override fun getItemCount() = weatherListData.hourly.size
}