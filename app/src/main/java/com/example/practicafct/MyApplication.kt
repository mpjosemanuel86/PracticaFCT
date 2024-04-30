package com.example.practicafct

import android.app.Application
import android.content.Context
import androidx.room.util.appendPlaceholders

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        lateinit var context:Context

    }
}