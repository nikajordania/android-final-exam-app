package com.example.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "temp") val temp: Double,
    @ColumnInfo(name = "humidity") val humidity: Int,
    @ColumnInfo(name = "time") val time: Int,
    @ColumnInfo(name = "timezone") val timezone: Int,
    @ColumnInfo(name = "icon") val icon: String
)