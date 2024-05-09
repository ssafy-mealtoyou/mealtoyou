package com.example.mealtoyou.handler

import android.util.Log
import com.example.mealtoyou.data.FcmData
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class FcmEventHandler{
    fun sendFcmToken(fcmToken: String){
        RetrofitClient.fcmInstance.postFcmData(
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVTTBCSFpOU3FMZENLN2hOV20xYnJnPT0iLCJpYXQiOjE3MTUxNDA4NzMsImV4cCI6MTcxNTIyNzI3M30.ZGIfU6HbKmcvvv75EzX0Y5uN2SaiAI8NTtpJ09yDsDk",
            FcmData(fcmToken=fcmToken, timeStamp = LocalDateTime.now())
        ).enqueue(object :
            Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API", "Data sent successfully")
                } else {
                    Log.e("API", "Failed to send data, response code: ${response.code()} + ${response.message()}")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error sending data: ${t.message}")
            }
        })
    }
}