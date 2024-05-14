package com.example.mealtoyou.retrofit

import com.example.mealtoyou.MainActivity
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.api.AuthApiService
import com.example.mealtoyou.api.CommunityApiService
import com.example.mealtoyou.api.FcmApiService
import com.example.mealtoyou.api.FoodSearchApiService
import com.example.mealtoyou.api.HealthApiService

import com.example.mealtoyou.api.SupplementApiService

import com.example.mealtoyou.auth.AuthInterceptor

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import com.example.mealtoyou.auth.LoggingInterceptor
import com.example.mealtoyou.handler.LocalTimeAdapter
import com.example.mealtoyou.ui.theme.group.ChatApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object RetrofitClient {
//    private const val BASE_URL = "http://70.12.247.142:8086/supplements/"

//    private const val BASE_URL = "http://192.168.0.25:8080/"
    private const val BASE_URL = "https://a102.mgbg.kr/"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(MainApplication.prefs.getValue("accessToken")))
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
        .setLenient()
        .create()

    val healthInstance: HealthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(HealthApiService::class.java)
    }

    val foodSearchInstance: FoodSearchApiService by lazy{
        val retrofit=Retrofit.Builder()
//            .baseUrl("http://70.12.247.142:8081/")
            .baseUrl("$BASE_URL:8084")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(FoodSearchApiService::class.java)
    }

    val fcmInstance: FcmApiService by lazy{
        val retrofit=Retrofit.Builder()
//            .baseUrl("http://70.12.247.142:8082/")
            .baseUrl("$BASE_URL")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(FcmApiService::class.java)
    }

    val chatInstance: ChatApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("$BASE_URL:8084/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChatApiService::class.java)
    }


    val supplementInstance: SupplementApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(SupplementApiService::class.java)
    }
    val authInstance: AuthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(AuthApiService::class.java)
    }

    val communityInstance: CommunityApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(CommunityApiService::class.java)

    }



}
