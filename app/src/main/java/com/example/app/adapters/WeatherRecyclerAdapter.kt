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
import com.example.app.api.dto.weatherdata.WeatherData
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*


class WeatherRecyclerAdapter(private val weatherListData: MutableList<WeatherData>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val weatherView: TextView = itemView.findViewById(R.id.weather)
        private val city: TextView = itemView.findViewById(R.id.city)
        private val humidity: TextView = itemView.findViewById(R.id.humidity)
        private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        private val frameLayout: FrameLayout = itemView.findViewById(R.id.weatherFrame)
        private val time: TextView = itemView.findViewById(R.id.time)
        private lateinit var weatherData: WeatherData
        private var calendar = Calendar.getInstance()
        private var dateFormat = SimpleDateFormat("HH:mm")


        @SuppressLint("SetTextI18n")
        fun onBind(weatherData: WeatherData) {
            calendar.timeInMillis = ((weatherData.dt + weatherData.timezone) * 1000).toLong()
            val hourDay = calendar.get(Calendar.HOUR_OF_DAY)

            Picasso.get().load(getIcon(weatherData.weather!![0].icon)).into(weatherIcon)
            weatherView.text = weatherData.main.temp.toInt().toString() + "℃"
            city.text = weatherData.name
            humidity.text = weatherData.main.humidity.toString() + "%"
            time.text = dateFormat.format(calendar.timeInMillis).toString()
            this.weatherData = weatherData

            if(hourDay in 6..14){
                constraintLayout.setBackgroundColor(Color.parseColor("#E0FEFF"))
                frameLayout.setBackgroundColor(Color.parseColor("#c3eff1"))
            } else if(hourDay in 15..19){
                constraintLayout.setBackgroundColor(Color.parseColor("#F7B511"))
                frameLayout.setBackgroundColor(Color.parseColor("#ffe29b"))
                weatherView.setTextColor(Color.parseColor("#FFFFFF"))
                city.setTextColor(Color.parseColor("#FFFFFF"))
                humidity.setTextColor(Color.parseColor("#FFFFFF"))
                time.setTextColor(Color.parseColor("#FFFFFF"))
            } else{
                constraintLayout.setBackgroundColor(Color.parseColor("#182078"))
                frameLayout.setBackgroundColor(Color.parseColor("#6a72c7"))
                weatherView.setTextColor(Color.parseColor("#FFFFFF"))
                city.setTextColor(Color.parseColor("#FFFFFF"))
                humidity.setTextColor(Color.parseColor("#FFFFFF"))
                time.setTextColor(Color.parseColor("#FFFFFF"))
            }
        }

        private fun getIcon(id: String): String {
            return "https://openweathermap.org/img/wn/$id@4x.png"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_weather_recycler, parent, false)

        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(weatherListData[position])
    }

    override fun getItemCount() = weatherListData.size
}