package com.example.mealtoyou.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context:Context) {
    private val prefs: SharedPreferences=context.getSharedPreferences("name",Context.MODE_PRIVATE)

    fun getValue(key: String) : String {
        return prefs.getString(key, "").toString()
    }
    fun setValue(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}