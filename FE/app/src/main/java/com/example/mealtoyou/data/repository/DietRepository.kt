package com.example.mealtoyou.data.repository

import android.graphics.Bitmap
import com.example.mealtoyou.data.FoodDetectionResponseItem
import com.example.mealtoyou.data.FoodRequestItem
import com.example.mealtoyou.retrofit.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.ByteArrayOutputStream

object DietRepository {
    private val dietApiService by lazy {
        RetrofitClient.dietInstance
    }

    suspend fun createDiet(foodRequestItemList: List<FoodRequestItem>): Response<Void> {
        return dietApiService.createDiet(foodRequestItemList)
    }

}

object DietImageRepository {
    private val dietImageApiService by lazy {
        RetrofitClient.dietImageInstance
    }

    private fun bitmapToMultiPart(bitmap: Bitmap, fileName: String): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val requestBody = byteArrayOutputStream.toByteArray().toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArrayOutputStream.size())
        return MultipartBody.Part.createFormData("image", fileName, requestBody)
    }

    suspend fun analyzeImage(bitmap: Bitmap, fileName: String): Response<List<FoodDetectionResponseItem>> {
        return dietImageApiService.analyzeImage(bitmapToMultiPart(bitmap, fileName))
    }

}