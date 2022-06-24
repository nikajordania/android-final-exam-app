package com.example.app.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room

class App(context: Context) : Application() {
    var db: AppDatabase

    companion object{
        var instance : App? = null
        private set
    }

    init{
        instance = this

        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database"
        ).allowMainThreadQueries().build()
    }
}