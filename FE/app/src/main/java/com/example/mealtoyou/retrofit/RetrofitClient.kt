package com.example.mealtoyou.retrofit

import com.example.mealtoyou.api.FcmApiService
import com.example.mealtoyou.api.FoodSearchApiService
import com.example.mealtoyou.api.HealthApiService
import com.example.mealtoyou.api.SupplementApiService
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.example.mealtoyou.auth.LoggingInterceptor
import com.example.mealtoyou.ui.theme.group.ChatApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object RetrofitClient {
//    private const val BASE_URL = "http://70.12.247.142:8086/supplements/"

    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(AuthInterceptor("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVTTBCSFpOU3FMZENLN2hOV20xYnJnPT0iLCJpYXQiOjE3MTUxNDA4NzMsImV4cCI6MTcxNTIyNzI3M30.ZGIfU6HbKmcvvv75EzX0Y5uN2SaiAI8NTtpJ09yDsDk"))
        .addInterceptor(LoggingInterceptor())  // LoggingInterceptor 추가
        .build()
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, JsonSerializer<LocalDate> { src, _, _ ->
            JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
        })
        .registerTypeAdapter(LocalDate::class.java, JsonDeserializer { json, _, _ ->
            LocalDate.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ISO_LOCAL_DATE)
        })
//        .registerTypeAdapter(LocalTime::class.java, JsonSerializer<LocalTime> { src, _, _ ->
//            JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
//        })
//        .registerTypeAdapter(LocalTime::class.java, JsonDeserializer { json, _, _ ->
//            LocalTime.parse(json.asJsonPrimitive.asString, DateTimeFormatter.ofPattern("HH:mm:ss"))
//        })
        .create()

    val healthInstance: HealthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://70.12.247.142:8083/supplements/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(HealthApiService::class.java)
    }

    val foodSearchInstance: FoodSearchApiService by lazy{
        val retrofit=Retrofit.Builder()
            .baseUrl("http://70.12.247.142:8081/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(FoodSearchApiService::class.java)
    }

    val fcmInstance: FcmApiService by lazy{
        val retrofit=Retrofit.Builder()
            .baseUrl("http://70.12.247.142:8082/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(FcmApiService::class.java)
    }

    val chatInstance: ChatApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://70.12.247.142:8084/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChatApiService::class.java)
    }

    val supplementInstance: SupplementApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://70.12.247.142:8086/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(SupplementApiService::class.java)
    }
}
