package com.example.mealtoyou.ui.theme.main.food

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.main.food.nomal.FoodItems
import com.example.mealtoyou.ui.theme.main.food.nomal.NutrientInfo
import com.example.mealtoyou.ui.theme.main.food.nomal.RecommendationBox
import com.example.mealtoyou.ui.theme.main.food.nomal.RecommendationHeader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun FoodBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DateLabel()
        RecommendationBox(true)
    }
}

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
) {
    Column(Modifier.padding(12.dp)) {
        RecommendationHeader()
        CalorieInfo("862kcal")
        NutrientInfo()
        FoodItems(showTemp, selectedItem, editable)
    }
}

