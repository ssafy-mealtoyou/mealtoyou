package com.example.mealtoyou.handler

import android.util.Log
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserEventHandler {
    fun sendUserWeight(weight: String) {
        RetrofitClient.userInstance.putUserWeight(mapOf("weight" to weight))
            .enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("API", "Data sent successfully")
                    } else {
                        Log.e(
                            "API",
                            "Failed to send data, response code: ${response.code()} + ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Error sending data: ${t.message}")
                }
            })
    }

    fun sendUserIntermittent(intermittentYn: String, startTime: String, endTime: String) {
        RetrofitClient.userInstance.putUserIntermittent(
            mapOf(
                "intermittentYn" to intermittentYn,
                "startTime" to startTime,
                "endTime" to endTime
            )
        )
            .enqueue(object :
                Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Log.d("API", "Data sent successfully")
                    } else {
                        Log.e(
                            "API",
                            "Failed to send data, response code: ${response.code()} + ${response.message()}"
                        )
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API", "Error sending data: ${t.message}")
                }
            })
    }
}