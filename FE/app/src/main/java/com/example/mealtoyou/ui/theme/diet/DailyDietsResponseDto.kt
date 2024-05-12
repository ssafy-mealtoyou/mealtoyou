package com.example.mealtoyou.ui.theme.diet

data class DailyDietsResponseDto(
    val type: String?,
    val date: String?,
    val dailyCaloriesBurned: Double?,
    val dailyCarbohydrateTaked:Double?,
    val dailyProteinTaked:Double?,
    val dailyFatTaked:Double?,
    val diets: List<Diet>
)
data class Diet(
    val dietId: Int,
    val totalCalories: Int,
    val carbohydratePer: Int,
    val proteinPer: Int,
    val fatPer: Int,
    val dietFoods: List<DietFood>
)

data class DietFood(
    val name: String,
    val imageUrl: String,
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double
)
