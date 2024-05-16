package com.example.mealtoyou.data.repository

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context:Context) {
    private val prefs: SharedPreferences=context.getSharedPreferences("name",Context.MODE_PRIVATE)

    fun getValue(key: String) : String {
        return prefs.getString(key, "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDNW9LaVlLTlJYTCtJNWhvTEJsUW5nPT0iLCJpYXQiOjE3MTU0ODI1NDAsImV4cCI6MTcyMzI1ODU0MH0.xjix3Z-xEogbiBjD0CNTVUXLmPdmns2NgX5DIcx5fqs").toString()
    }
    fun setValue(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun removeValue(key: String) {
        prefs.edit().remove(key).apply()
    }
}