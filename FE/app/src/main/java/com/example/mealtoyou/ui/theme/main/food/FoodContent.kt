package com.example.mealtoyou.ui.theme.main.food

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.main.food.nomal.FoodItems
import com.example.mealtoyou.ui.theme.main.food.nomal.NutrientInfo
import com.example.mealtoyou.ui.theme.main.food.nomal.RecommendationHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateLabel() {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE dd MMM", Locale.ENGLISH)
    val dateString = formatter.format(today).uppercase(Locale.ROOT)
    Text(
        text = dateString,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Pretend,
        color = Color(0xff565d6d),
        modifier = Modifier.padding(start = 20.dp)
    )
}

@Composable
fun CalorieInfo(calories: String) {
    Text(
        text = calories,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Pretend,
        color = Color(0xFF171A1F)
    )
}

@Composable
fun NormalContent(
    showTemp: MutableState<Boolean>,
    selectedItem: MutableState<String>,
    editable: Boolean,
    diet: Diet,
) {
    Column(Modifier.padding(12.dp)) {
        RecommendationHeader()
        CalorieInfo(diet.totalCalories.toString() + "kcal")
        NutrientInfo(diet)

        if (diet.dietFoods != null) {
            FoodItems(showTemp, selectedItem, editable, diet.dietFoods)
        }
        else {
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = "이런! 불러온 식단 목록이 없어요!",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Pretend,
                lineHeight = 17.sp,
                color = Color(0xFF171A1F)
            )
        }
    }
}

