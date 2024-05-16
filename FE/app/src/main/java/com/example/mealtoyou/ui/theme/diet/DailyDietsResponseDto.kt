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
        var totalCalories: Int,
        var carbohydratePer: Int,
        var proteinPer: Int,
        var fatPer: Int,
        var dietFoods: List<DietFood>?
)

data class DietFood(
    val name: String,
    val imageUrl: String,
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double
)
