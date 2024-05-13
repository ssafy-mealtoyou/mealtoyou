package com.example.mealtoyou.data.repository


import android.util.Log
import com.example.mealtoyou.data.SupplementResponseData
import com.example.mealtoyou.retrofit.RetrofitClient

object SupplementRepository {
    private val supplementResult: MutableList<SupplementResponseData> = mutableListOf()

    suspend fun getSupplements(): MutableList<SupplementResponseData>? {
        Log.d("a","hi")
        supplementResult.clear()  // 리스트를 함수 호출마다 초기화
        val response = RetrofitClient.supplementInstance.getSupplements("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJVTTBCSFpOU3FMZENLN2hOV20xYnJnPT0iLCJpYXQiOjE3MTUzMDU2MTEsImV4cCI6MTcxNTM5MjAxMX0.drxhJLeBWYlSAFjJZippQtEgkmnHUambHPenUEx83FY")
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
}