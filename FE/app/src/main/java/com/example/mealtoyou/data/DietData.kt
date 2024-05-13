package com.example.mealtoyou.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class FoodRequestItem(
    val foodId: Long,
    val servingSize: Double
)

@Parcelize
data class SwipeFoodItemModel(
    val fid: Long = -1,
    val itemName: String = "",
    val itemImageUrl: String = "",
    val energy: Double = 0.0,
    val quantity: Double = 0.0
) : Parcelable

