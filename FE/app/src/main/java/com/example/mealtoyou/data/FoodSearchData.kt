package com.example.mealtoyou.data

import com.google.gson.annotations.SerializedName

data class FoodSearchData(
    val id: Long,
    val name: String,
    @SerializedName("serving_unit")
    val servingUnit: String,
    @SerializedName("serving_size")
    val servingSize: Double,
    val protein: Double,
    val energy: Double,
    val fat: Double,
    val carbohydrate: Double,
)