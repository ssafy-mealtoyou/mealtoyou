package com.example.mealtoyou.ui.theme.main.food

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun FoodBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        DateLabel("TUES 11 JUL")
        RecommendationBox()
    }
}

@Composable
fun DateLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = Pretend,
        color = Color(0xff565d6d),
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
fun NormalContent(showTemp: MutableState<Boolean>, selectedItem: MutableState<String>) {
    Column(Modifier.padding(12.dp)) {
        RecommendationHeader()
        CalorieInfo("862kcal")
        NutrientInfo()
        FoodItems(showTemp, selectedItem)
    }
}

@Composable
fun Temp(selectedItem: String, showTemp: MutableState<Boolean>) {
    Column {
        Text("Selected Item: $selectedItem")
        Button(
            onClick = { showTemp.value = false },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
            shape = RoundedCornerShape(12),
            modifier = Modifier
                .width(100.dp)
                .height(40.dp)
        ) {
            Text("돌아가기", color = Color.White)
        }
    }
}