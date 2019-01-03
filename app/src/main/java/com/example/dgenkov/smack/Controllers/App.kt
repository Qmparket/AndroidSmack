package com.example.dgenkov.smack.Controllers

import android.app.Application
import com.example.dgenkov.smack.Utilities.SharedPrefs

class App: Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }
    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}