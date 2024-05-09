package com.example.mealtoyou.ui.theme.main.food.nomal

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.mealtoyou.R
import com.example.mealtoyou.data.repository.FoodSearchRepository
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.shadowModifier
import com.example.mealtoyou.viewmodel.FoodSearchViewModel
import java.text.DecimalFormat


@Composable
fun IncrementDecrementButtons() {
    var quantity by remember { mutableDoubleStateOf(1.0) } // 초기값 설정

    // DecimalFormat을 사용하여 소숫점 첫 번째 자리에서 반올림
    val df = DecimalFormat("#.#")

    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically // 수직 정렬을 가운데로 설정
    ) {
        Image(
            painter = painterResource(id = R.drawable.minusg),
            contentDescription = "Minus Button",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    if (quantity > 0.1) quantity -= 0.1 // 최소값을 넘지 않도록 조건 설정
                }
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            "${df.format(quantity)} 인분",
            color = Color(0xFF171A1F),
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.width(6.dp))
        Image(
            painter = painterResource(id = R.drawable.plusbuttonp),
            contentDescription = "Plus Button",
            modifier = Modifier
                .size(30.dp)
                .clickable {
                    quantity += 0.1 // 0.1 증가
                }
        )
    }
}

@Composable
fun FoodItemSearch(name:String,energy:Double) {
    Box(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
    ) {
        Row {
            Box(
                modifier = shadowModifier()
                    .padding(10.dp)
                    .weight(1f)
                    .height(70.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_food),
                            contentDescription = "Sample Food Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "item",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF171A1F)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "215 Kcal", fontSize = 10.sp, color = Color(0xFF171A1F)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12),
                        modifier = Modifier
                            .width(78.dp)
                            .fillMaxHeight()
                            .padding(0.dp)
                    ) {
                        Text(
                            text = "선택",
                            color = Color(0xFF6D31ED),
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeFoodItem(item: String, onRemoveItem: () -> Unit) {
    val swipeableState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(LocalDensity.current) { 70.dp.toPx() }
    val anchors = mapOf(0f to 0, -sizePx to 1)

    val visibleSize = (-swipeableState.offset.value).coerceAtLeast(0f)
    val deleteButtonWidth by animateDpAsState(
        targetValue = (visibleSize / sizePx * 70.dp).coerceAtMost(
            70.dp
        )
    )
    val spacerWidth = if (visibleSize >= 1) 8.dp else 0.dp

    Box(
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Row {
            Box(
                modifier = shadowModifier()
                    .weight(1f)
                    .height(70.dp)
                    .padding(10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Transparent)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_food),
                            contentDescription = "Sample Food Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = item,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF171A1F)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "215 Kcal", fontSize = 10.sp, color = Color(0xFF171A1F)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IncrementDecrementButtons()
                }

            }
            Spacer(modifier = Modifier.width(spacerWidth))
            Box(
                modifier = Modifier
                    .width(deleteButtonWidth)
                    .height(70.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFDE3B40))
                    .align(Alignment.CenterVertically)
                    .clickable { onRemoveItem() }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minus),
                    contentDescription = "minus button",
                    modifier = Modifier
                        .size(26.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

fun Modifier.dashedBorder(
    color: Color, strokeWidth: Float, dashLength: Float, gapLength: Float
): Modifier = this.then(object : DrawModifier {
    override fun ContentDrawScope.draw() {
        drawContent()
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashLength, gapLength), phase = 0f)

        val paint = Paint().apply {
            this.color = color
            this.pathEffect = pathEffect
            this.style = PaintingStyle.Stroke
            this.strokeWidth = strokeWidth
            this.strokeCap = StrokeCap.Round // 선의 끝을 둥글게 처리
        }

        drawIntoCanvas { canvas ->
            canvas.drawRect(
                left = 0f, top = 0f, right = size.width, bottom = size.height, paint = paint
            )
        }
    }
})

@Composable
private fun FoodBottomSheetContent(setContent: (String) -> Unit, imageBoolean: Boolean) {
    val viewModel: FoodSearchViewModel = viewModel()
    var showDialog by remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf("") }
    val foodSearchResult = viewModel.foodSearchResult.value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        if (!showDialog) {
            if (imageBoolean) {
                Box(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sample_food),
                        contentDescription = "Sample Food Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .dashedBorder(
                        color = Color(0xFF565D6D),
                        strokeWidth = 2f,
                        dashLength = 10f,
                        gapLength = 10f
                    )
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center  // 상하좌우 가운데 정렬

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically  // Row 내부 요소를 수직으로 가운데 정렬
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.plus),
                        contentDescription = "Add Icon",
                        modifier = Modifier.size(28.dp)
                    )
                    Text(
                        text = "검색해서 추가하기",
                        color = Color(0xFF323743),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 8.dp)  // 텍스트와 이미지 사이의 간격 추가
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            SwipeFoodItems() // 스크롤 가능한 아이템 리스트를 호출
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { setContent("default") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED))
                ) {
                    Text(
                        "등록하기", fontSize = 16.sp, color = Color.White
                    )
                }
            }
        } else {
            Text(
                text = "음식 검색",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            OutlinedTextField(
                value = textState.value,  // 현재 텍스트 상태
                onValueChange = { textState.value = it },  // 텍스트가 변경될 때 상태 업데이트
                label = { Text("Enter text") },  // 라벨 텍스트
                singleLine = true,  // 단일 줄 입력 필드
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // 여기에 서버로 데이터를 전송하는 코드를 추가합니다.
                        Log.d("foodSearchKeyword",textState.value)
                        viewModel.foodSearch(textState.value)
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp)
            )
            foodSearchResult?.let { resultList ->
                LazyColumn {
                    items(resultList) { foodItem ->
                        Spacer(modifier = Modifier.height(15.dp))
                        FoodItemSearch(name = foodItem.name,energy=foodItem.energy)
                    }
                }
            } ?: run {
                // 결과가 null인 경우, 예를 들어 "검색 결과가 없습니다"와 같은 메시지 표시
                Text(text = "검색 결과가 없습니다.")
            }
//            Spacer(modifier = Modifier.height(15.dp))
//            FoodItemSearch()
//            Spacer(modifier = Modifier.height(15.dp))
//            FoodItemSearch()
//            Spacer(modifier = Modifier.height(15.dp))
//            FoodItemSearch()
//            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { setContent("default") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED))
            ) {
                Text(
                    "등록하기", fontSize = 16.sp, color = Color.White
                )
            }
        }
    }

}

@Composable
fun AddDiet() {
    val (content, setContent) = remember { mutableStateOf("default") }

    when (content) {
        "default" -> {
            Column {
                Item(text = "사진으로 등록하기",
                    iconId = R.drawable.camera,
                    onItemClicked = { setContent("photo") })
                Spacer(modifier = Modifier.height(20.dp))
                Item(text = "직접 등록하기", iconId = R.drawable.search,
                    onItemClicked = {
                        setContent("no-photo")
                    })
            }
        }

        "no-photo" -> {
            FoodBottomSheetContent(setContent, false)
        }

        "photo" -> {
            FoodBottomSheetContent(setContent, true)
        }
    }
}

@Composable
fun AnimatedArrowIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val arrowOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 5f, animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing), // 500ms 동안 완료
            repeatMode = RepeatMode.Reverse // 반복 모드 설정
        ), label = ""
    )

    IconButton(onClick = { /* 스크롤을 최하단으로 이동시킬 동작 구현 */ }) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "More items below",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .offset(y = arrowOffset.dp), // Y축 방향으로 offset 적용
            tint = Color(0xFF6D31ED) // 커스텀 색상 적용
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun SwipeFoodItems() {
    var items by remember { mutableStateOf(List(10) { "Item $it" }) }  // 상태로 관리되는 아이템 리스트
    val lazyListState = rememberLazyListState()  // 스크롤 상태 관리

    val showIndicator = derivedStateOf {
        val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        lastVisibleItem < items.size
    }

    fun removeItem(item: String) {
        items = items.filter { it != item }
        Log.d("WOrk", "Working")
    }

    Box {
        Column {
            LazyColumn(modifier = Modifier.height(230.dp), state = lazyListState) {
                items(items, key = { it }) { item ->
                    SwipeFoodItem(item = item, onRemoveItem = { removeItem(item) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    visible = showIndicator.value, enter = fadeIn() + expandVertically(
                        animationSpec = tween(durationMillis = 300), expandFrom = Alignment.Top
                    ), exit = fadeOut() + shrinkVertically(
                        animationSpec = tween(durationMillis = 300),
                        shrinkTowards = Alignment.Bottom
                    )
                ) {
                    AnimatedArrowIcon()
                }
            }
        }
    }
}

@Composable
fun Item(text: String, iconId: Int, onItemClicked: () -> Unit) {
    Column(
        modifier = shadowModifier()
            .fillMaxWidth()
            .height(165.dp)
            .padding(10.dp)
            .clickable(onClick = onItemClicked), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = text,
            lineHeight = 26.sp,
            fontSize = 16.sp,
            color = Color(0xFF171A1F),
            textAlign = TextAlign.Center
        )
        Image(
            painter = painterResource(id = iconId),
            contentDescription = text,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun FoodItems(
    showTemp: MutableState<Boolean>,
    selectedItem: MutableState<String>,
    editable: Boolean
) {
    val koreanFoods = listOf("김치", "불고기", "비빔밥", "된장찌개", "김밥", "떡볶이")
    var sheetOpen by remember {
        mutableStateOf(false)
    }
    if (sheetOpen) {
        BottomSheet(closeSheet = { sheetOpen = false }, { AddDiet() })
    }
    Column {
        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) { index ->
                FoodItem(koreanFoods[index], showTemp, selectedItem)
                if (index < 2) Spacer(Modifier.weight(1f))
            }
        }

        Row(modifier = Modifier.padding(top = 12.dp)) {
            repeat(3) { index ->
                FoodItem(koreanFoods[index + 3], showTemp, selectedItem)
                if (index < 2) Spacer(Modifier.weight(1f))
            }
        }
        if (editable) {
            Row {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        sheetOpen = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
                    shape = RoundedCornerShape(12),
                    modifier = Modifier
                        .width(100.dp)
                        .height(55.dp)
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = "등록하기",
                        color = Color.White,
                    )
                }
            }
        }

    }
}

@Composable
fun FoodItem(
    itemName: String, showTemp: MutableState<Boolean>, selectedItem: MutableState<String>
) {
    Box(modifier = Modifier
        .height(120.dp)
        .width(104.dp)
        .clip(RoundedCornerShape(8.dp))
        .clickable {
            selectedItem.value = itemName
            showTemp.value = true
        }) {
        Image(
            painter = painterResource(id = R.drawable.tm),
            contentDescription = "Background Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(Modifier.padding(6.dp)) {
            Box(
                modifier = Modifier
                    .background(
                        Color.White, shape = RoundedCornerShape(8.dp)
                    ) // 흰색 배경과 둥근 모서리 적용
                    .padding(horizontal = 6.dp, vertical = 2.dp) // 텍스트 주변의 패딩
            ) {
                Text(
                    text = itemName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Pretend,
                    lineHeight = 17.sp,
                    color = Color(0xFF171A1F)
                )
            }
            Spacer(Modifier.height(13.dp))
            Box(
                modifier = Modifier
                    .background(
                        Color.DarkGray, shape = RoundedCornerShape(8.dp)
                    ) // 흰색 배경과 둥근 모서리 적용
                    .padding(horizontal = 5.dp, vertical = 1.dp) // 텍스트 주변의 패딩
            ) {
                Text(
                    text = "215kcal",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = Pretend,
                    color = Color.White
                )
            }
        }
    }
}