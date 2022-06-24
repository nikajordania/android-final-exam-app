package com.example.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ForecastDao {
    @Query("SELECT * FROM forecast")
    fun getRecords() : List<Forecast>

    @Insert
    fun saveRecord(record: Forecast)

    @Query("DELETE FROM forecast")
    fun resetRecords()
}