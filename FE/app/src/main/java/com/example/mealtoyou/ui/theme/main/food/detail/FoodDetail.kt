package com.example.mealtoyou.ui.theme.main.food.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.diet.Diet
import com.example.mealtoyou.ui.theme.shared.CloseButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Buttons() {
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
            onClick = { },
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
    selectedItem: String,
    showTemp: MutableState<Boolean>,
    editable: Boolean,
    diet: Diet
) {
    val isLoading = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(Modifier.padding(12.dp)) {

        if (isLoading.value) {
            LoadingSpinner()
        } else {
            DetailHeader(selectedItem, showTemp)
            ContentBody(editable)
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
private fun OverlayImage(text: String) {
    Box(
        modifier = Modifier
            .height(72.dp)
            .width(75.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.sample_food),
            contentDescription = "Sample Food Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0xFF171A1F).copy(alpha = 0.7f))
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontFamily = Pretend,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ContentBody(editable: Boolean) {
    Column {
        Spacer(modifier = Modifier.height(10.dp))
        Row {
            Image(
                painter = painterResource(id = R.drawable.sample_food),
                contentDescription = "Sample Food Image",
                modifier = Modifier
                    .height(230.dp)
                    .fillMaxWidth(0.745f)
                    .clip(RoundedCornerShape(8.dp)),

                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.weight(1f))
            Column {
                OverlayImage("돈까스")
                Spacer(modifier = Modifier.height(7.dp))
                OverlayImage("치킨")
                Spacer(modifier = Modifier.height(7.dp))
                OverlayImage("바오밥나무")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            Row {
                InnerText(text = "탄수화물")
                Spacer(modifier = Modifier.weight(1f))
                InnerText(text = "102g")
            }
            Row {
                InnerText(text = "단백질")
                Spacer(modifier = Modifier.weight(1f))
                InnerText(text = "102g")
            }
            Row {
                InnerText(text = "지방")
                Spacer(modifier = Modifier.weight(1f))
                InnerText(text = "102g")
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (editable) {
            Buttons()
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
