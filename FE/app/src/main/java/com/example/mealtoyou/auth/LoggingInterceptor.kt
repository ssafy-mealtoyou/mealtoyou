package com.example.mealtoyou.auth

import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log

class LoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("API Request", "URL: ${request.url}")  // 요청 URL을 로그로 출력
        return chain.proceed(request)
    }
}