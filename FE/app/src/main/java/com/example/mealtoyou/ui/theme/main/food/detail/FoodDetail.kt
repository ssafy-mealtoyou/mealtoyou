package com.example.mealtoyou.ui.theme.main.food.detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.R
import com.example.mealtoyou.data.FoodNutrient
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.diet.DietFood
import com.example.mealtoyou.ui.theme.group.defaultShadow
import com.example.mealtoyou.ui.theme.shared.CloseButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Buttons(onUpdate: (DietFood) -> Unit, currentDiet: DietFood) {
    Row {
        Spacer(Modifier.weight(1f))
        ActionButton(
            text = "삭제하기",
            onClick = { },
            color = Color.Red,
            backgroundColor = Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        ActionButton(
            text = "변경하기",
            onClick = { onUpdate(currentDiet) },
            color = Color.White,
            backgroundColor = Color(0xFF6D31ED)
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    backgroundColor: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12),
        modifier = Modifier
            .width(80.dp)
            .height(40.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontFamily = Pretend,
            fontSize = 14.sp,
            lineHeight = 14.sp
        )
    }
}


@Composable
fun FoodDetail(
    dietFoods: List<DietFood>,
    selectedItem: String,
    showTemp: MutableState<Boolean>,
    editable: Boolean,
    diets: MutableState<List<DietFood>>,
    onUpdate: (DietFood) -> Unit

) {
    val isLoading = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val selectedFood = dietFoods.find { it.name == selectedItem }
                selectedFood?.let { food ->
                    val result = RetrofitClient.dietInstance.recommendOtherFood(
                        FoodNutrient(
                            MainApplication.prefs.getValue("userId").toInt(),
                            food.name,
                            food.calories,
                            food.carbohydrate,
                            food.protein,
                            food.fat
                        )
                    )
                    // 결과를 로그에 기록합니다.
                    Log.d("FetchRecommendation", "Result: $result")

                    // 결과를 diets에 할당합니다.
                    diets.value = result

                    // 로딩 상태를 false로 설정합니다.
                    isLoading.value = false
                } ?: run {
                    // selectedFood가 null인 경우를 처리합니다.
                    Log.e("FetchRecommendation", "Selected food not found")
                    isLoading.value = false
                }
            } catch (e: Exception) {
                // 에러를 처리하고 로그에 기록합니다.
                Log.e("FetchRecommendation", "Error fetching recommendation", e)
                isLoading.value = false
            }
        }
    }
    val selectedDiet = dietFoods.find { it.name == selectedItem }
    var centralDiet by remember { mutableStateOf(selectedDiet) }

    Log.d("bbbbb", centralDiet.toString())


    Column(Modifier.padding(12.dp)) {

        if (isLoading.value) {
            LoadingSpinner()
        } else {
            Log.d("lllll", centralDiet.toString())
            DetailHeader(centralDiet?.name ?: "", showTemp)
            ContentBody(dietFoods, centralDiet, diets, editable, onSelectDiet = { newSelectedDiet ->
                centralDiet?.let { currentCentralDiet ->
                    // Swap the central diet with the selected diet
                    val updatedDiets = diets.value.toMutableList()
                    val index = updatedDiets.indexOfFirst { it.name == newSelectedDiet.name }
                    if (index >= 0) {
                        updatedDiets[index] = currentCentralDiet
                        diets.value = updatedDiets
                        centralDiet = newSelectedDiet
                    }
                }
            },
                onUpdate = { dietFood ->
                    onUpdate(dietFood)
                    showTemp.value = false // Close the screen
                }
            )

        }
    }

    LoadContentEffect(coroutineScope, isLoading)
}

@Composable
private fun DetailHeader(selectedItem: String, showTemp: MutableState<Boolean>) {
    Row {
        Text(
            selectedItem,
            color = Color(0xff171A1F),
            fontFamily = Pretend,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        CloseButton(showTemp)
    }
}

@Composable
private fun LoadingSpinner() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            color = Color(0xFF5686FF),
            modifier = Modifier.size(60.dp),
            strokeWidth = 6.dp
        )
    }
}


@Composable
private fun InnerText(text: String) {
    Text(
        text = text,
        fontFamily = Pretend,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        color = Color(0xFF171A1F)
    )
}

@Composable
private fun OverlayImage(diet: DietFood, onSelect: (DietFood) -> Unit) {
    Box(
        modifier = Modifier
            .defaultShadow()
            .height(44.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onSelect(diet) } // Click event added
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFFF5F1FE))
        )
        Text(
            text = diet.name,
            color = Color(0xff171A1F),
            fontSize = 14.sp,
            fontFamily = Pretend,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ContentBody(
    dietFoods: List<DietFood>,
    centralDiet: DietFood?,
    diets: MutableState<List<DietFood>>,
    editable: Boolean,
    onSelectDiet: (DietFood) -> Unit,
    onUpdate: (DietFood) -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))

        centralDiet?.let {
            Column(
                modifier = Modifier
                    .defaultShadow()
                    .fillMaxWidth()
                    .background(Color(0xffF5F1FE))
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row {
                    InnerText(text = "탄수화물")
                    Spacer(modifier = Modifier.weight(1f))
                    InnerText(text = String.format("%.2f g", it.carbohydrate))
                }
                Row {
                    InnerText(text = "단백질")
                    Spacer(modifier = Modifier.weight(1f))
                    InnerText(text = String.format("%.2f g", it.protein))
                }
                Row {
                    InnerText(text = "지방")
                    Spacer(modifier = Modifier.weight(1f))
                    InnerText(text = String.format("%.2f g", it.fat))
                }

            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (diets.value.isNotEmpty()) {
            Text(
                "대체 식품 추천",
                color = Color(0xff171A1F),
                fontFamily = Pretend,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                diets.value.take(3).forEach { diet ->
                    OverlayImage(diet, onSelect = {
                        onSelectDiet(diet)
                        Log.d("ContentBody", "Selected Diet: ${diet.name}")
                    })
                    Spacer(modifier = Modifier.height(7.dp))
                }

            }
            Spacer(modifier = Modifier.height(12.dp))


            Spacer(modifier = Modifier.weight(1f))
            if (editable) {
                Buttons(onUpdate = { onUpdate(centralDiet!!) }, currentDiet = centralDiet!!)
            }
        }

    }
}

@Composable
private fun LoadContentEffect(coroutineScope: CoroutineScope, isLoading: MutableState<Boolean>) {
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(500)  // 이미지 로딩 시뮬레이션
            isLoading.value = false
        }
    }
}
