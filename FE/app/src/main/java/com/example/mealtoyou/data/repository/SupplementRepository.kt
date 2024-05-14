package com.example.mealtoyou.data.repository


import android.util.Log
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.data.SupplementRequestData
import com.example.mealtoyou.data.SupplementResponseData
import com.example.mealtoyou.retrofit.RetrofitClient
import retrofit2.Response

object SupplementRepository {
    private val supplementResult: MutableList<SupplementResponseData> = mutableListOf()

    suspend fun getSupplements(): MutableList<SupplementResponseData>? {
        supplementResult.clear()  // 리스트를 함수 호출마다 초기화
        val response = RetrofitClient.supplementInstance.getSupplements()
//        Log.d("response","${response.body()}")
        if (response.isSuccessful) {
//            Log.d("데이터 결과", response.body().toString())

            response.body()?.let{ responseBody->
                supplementResult.addAll(responseBody)
            }
        } else {
            Log.e("err","${response.body()}")
        }
        return supplementResult;
    }

    suspend fun registerSupplements(dataList: List<SupplementRequestData>): String {
        try {
            val response = RetrofitClient.supplementInstance.registerSupplements(dataList)
            Log.d("data","${dataList.get(0).alertTime}")

            if (response.isSuccessful) {
                return "Data sent successfully"
            } else {
                Log.e("API Error", "Error sending data: ${response.errorBody()?.string()}")
                return "Failed to send data"
            }
        } catch (e: Exception) {
            Log.e("API Error", "Exception in sending data: ${e.message}")
            return "Error occurred while sending data"
        }
    }

}