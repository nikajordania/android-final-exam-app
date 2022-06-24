package com.example.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Weather::class, Forecast::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
    abstract fun forecastDao(): ForecastDao
}