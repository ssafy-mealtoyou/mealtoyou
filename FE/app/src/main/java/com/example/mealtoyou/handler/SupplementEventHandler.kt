package com.example.mealtoyou.handler

import android.util.Log
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplementEventHandler {
    fun sendSupplementTaken(takenYn: String) {
        RetrofitClient.supplementInstance.patchSupplement(takenYn)
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