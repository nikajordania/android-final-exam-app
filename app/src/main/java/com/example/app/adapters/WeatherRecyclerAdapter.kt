package com.example.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app.R
import com.example.app.api.dto.weatherdata.WeatherData
import com.squareup.picasso.Picasso


class WeatherRecyclerAdapter(private val weatherListData: MutableList<WeatherData>) :
    RecyclerView.Adapter<WeatherRecyclerAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val weatherIcon: ImageView = itemView.findViewById(R.id.weatherIcon)
        private val weatherView: TextView = itemView.findViewById(R.id.weather)
        private val city: TextView = itemView.findViewById(R.id.city)
        private val humidity: TextView = itemView.findViewById(R.id.humidity)
        private lateinit var weatherData: WeatherData


        @SuppressLint("SetTextI18n")
        fun onBind(weatherData: WeatherData) {
            Picasso.get().load(getIcon(weatherData.weather[0].icon)).into(weatherIcon)
            weatherView.text = weatherData.main.temp.toInt().toString() + "℃"
            city.text = weatherData.name
            humidity.text = weatherData.main.humidity.toString() + "%"
            this.weatherData = weatherData
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