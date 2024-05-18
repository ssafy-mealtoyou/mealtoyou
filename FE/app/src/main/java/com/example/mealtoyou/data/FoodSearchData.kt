package com.example.mealtoyou.data

import com.google.gson.annotations.SerializedName

data class FoodSearchData(
    val id: Long,
    val name: String,
    @SerializedName("serving_unit")
    val servingUnit: String,
    @SerializedName("serving_size")
    val servingSize: Double,
    @SerializedName("protein(g)")
    val protein: Double,
    @SerializedName("energy(kcal)")
    val energy: Double,
    @SerializedName("fat(g)")
    val fat: Double,
    @SerializedName("carbohydrate(g)")
    val carbohydrate: Double,
)