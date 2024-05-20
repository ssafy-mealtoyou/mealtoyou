package com.example.mealtoyou

import android.app.Application
import com.example.mealtoyou.data.repository.PreferenceUtil

class MainApplication:Application() {
    companion object {
        lateinit var prefs: PreferenceUtil
    }
    override fun onCreate() {
        super.onCreate()
        prefs = PreferenceUtil(applicationContext)
    }
}