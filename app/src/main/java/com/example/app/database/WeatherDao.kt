package com.example.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather")
    fun getRecords() : List<Weather>

    @Insert
    fun saveRecord(record: Weather)

    @Query("DELETE FROM weather")
    fun resetRecords()
}