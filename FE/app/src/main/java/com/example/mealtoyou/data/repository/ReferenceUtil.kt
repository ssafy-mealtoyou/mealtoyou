package com.example.mealtoyou.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.mealtoyou.MainApplication

class PreferenceUtil(context:Context) {
    private val prefs: SharedPreferences=context.getSharedPreferences("name",Context.MODE_PRIVATE)

    fun getValue(key: String) : String {
        return prefs.getString(key, "").toString()
    }
    fun setValue(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun removeValue(key: String) {
        prefs.edit().remove(key).apply()
    }
}