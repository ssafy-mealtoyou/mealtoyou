package com.example.mealtoyou.ui.theme.main.food.nomal

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.data.FoodNutrient
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.diet.DietFood
import com.example.mealtoyou.ui.theme.main.food.NormalContent
import com.example.mealtoyou.ui.theme.main.food.detail.FoodDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecommendationBox(editable: Boolean) {
    val height = if (editable) 450.dp else 400.dp

    Box(
        modifier = Modifier
                .height(height)
                .wrapContentWidth()
                .padding(bottom = 20.dp, top = 8.dp, start = 20.dp, end = 20.dp)
                .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        ambientColor = Color(0xFF171A1F),
                        spotColor = Color(0xFF171A1F)
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
    ) {
        RecommendationContent(editable, null)
    }
}

@Composable
fun RecommendationContent(editable: Boolean, diet: Diet?) {
    val showTemp = remember { mutableStateOf(false) }
    val selectedItem = remember { mutableStateOf("") }
    val diets = remember { mutableStateOf<List<DietFood>>(emptyList()) }

    Box(contentAlignment = Alignment.Center) {
        if (showTemp.value) {
            if (diet != null) {
                diet.dietFoods?.let { FoodDetail(it,selectedItem.value, showTemp, editable, diets) }
            }
        } else {
            if (diet != null) {
                NormalContent(showTemp, selectedItem, editable, diet)
            }
        }
    }
}


@Composable
fun RecommendationHeader() {
    Row {
        Text(
            text = "추천식단",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            color = Color(0xFF9095A1),
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}