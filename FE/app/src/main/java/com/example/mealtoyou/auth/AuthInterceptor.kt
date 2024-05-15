package com.example.mealtoyou.auth

import android.util.Log
import com.example.mealtoyou.MainApplication
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken= MainApplication.prefs.getValue("accessToken")
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()
        Log.e("auth","auth")
        Log.e("auth","${newRequest.header("Authorization")}")
        return chain.proceed(newRequest)
    }
}
