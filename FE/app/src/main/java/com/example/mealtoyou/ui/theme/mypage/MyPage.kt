package com.example.mealtoyou.ui.theme.group

import android.widget.NumberPicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.health.connect.client.HealthConnectClient
import com.example.mealtoyou.R
import com.example.mealtoyou.handler.HealthEventHandler
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.main.stage.Challenge
import com.example.mealtoyou.ui.theme.main.stage.DrugInfo
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.example.mealtoyou.ui.theme.shared.shadowModifier
import java.time.LocalTime
import java.time.format.DateTimeParseException

@Composable
fun MyPage() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val healthConnectClientState = remember { mutableStateOf<HealthConnectClient?>(null) }
    val healthEventHandlerState = remember { mutableStateOf<HealthEventHandler?>(null) }

    LaunchedEffect(Unit) {
        try {
            val healthConnectClient = HealthConnectClient.getOrCreate(context)
            healthConnectClientState.value = healthConnectClient
            healthEventHandlerState.value = HealthEventHandler(lifecycleOwner, healthConnectClient)
        } catch (e: Exception) {
            // Health Connect가 없을 때의 처리
            healthConnectClientState.value = null
            healthEventHandlerState.value = null
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {


            MainBar(text = "마이페이지", infoImg = false)

            val scrollState = rememberScrollState()
            Column(Modifier.verticalScroll(scrollState)) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.main_image),
                        contentDescription = "Sample Food Image",
                        modifier = Modifier
                            .height(211.dp)
                            .fillMaxWidth(0.55f)
                            .clip(CircleShape),

                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                Column(Modifier.padding(20.dp))
                {
                    Box(
                        modifier = shadowModifier()
                            .height(156.dp)
                            .fillMaxWidth()
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = Color(0xFF171A1F),
                                spotColor = Color(0xFF171A1F)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "나의 체성분 정보",
                                color = Color(0xFF323743),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "골격근량",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9095A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(15.dp))
                                Text(
                                    text = "32kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(20.dp))
                                Text(
                                    text = "체지방량",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9095A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(15.dp))
                                Text(
                                    text = "22kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                var addSheet by remember {
                                    mutableStateOf(false)
                                }
                                if (addSheet) {
                                    BottomSheet(
                                        closeSheet = { addSheet = false },
                                        { AddDetailWeight() })
                                }
                                Button(
                                    onClick = { addSheet = true },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                    border = BorderStroke(1.dp, color = Color(0xFF6D31ED)),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(101.dp),
                                ) {

                                    Text(
                                        text = "직접 등록",
                                        fontFamily = Pretend,
                                        color = Color(0xFF6D31ED)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {healthEventHandlerState.value?.readHealthData()},
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF6D31ED
                                        )
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(158.dp)
                                        .shadow(
                                            elevation = 10.dp,
                                            shape = RoundedCornerShape(8.dp),
                                            clip = true,
                                            ambientColor = Color(0xFF171A1F).copy(alpha = 0.15f),
                                            spotColor = Color(0xFF171A1F).copy(alpha = 0.15f)
                                        ),
                                ) {
                                    Text(
                                        text = "갤럭시 워치로 등록",
                                        fontFamily = Pretend,
                                        color = Color.White
                                    )
                                }
                            }

                        }
                    }

                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .padding(end = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    StopEatTimer(Modifier.weight(9f), Color(0xFFF1FDE9), true)

                    Spacer(Modifier.weight(1f))
                    EditWeight(Modifier.weight(9f), Color(0xFFFFFAE1), true)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .padding(end = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    Challenge(Modifier.weight(9f), Color(0xFFF5F1FE), true)

                    Spacer(Modifier.weight(1f))
                    DrugInfo(Modifier.weight(9f), Color(0xFFF0F9FF), true)
                }
            }

        }

    }
}

@Composable
private fun CustomTextField() {
    val text = remember { mutableStateOf("") }
    val textStyle = TextStyle(
        color = Color(0xFF171A1F),
        fontSize = 16.sp,
        lineHeight = 50.sp,  // 이 값은 필요에 따라 조정할 수 있습니다.
        textAlign = TextAlign.Center,
    )

    BasicTextField(
        value = text.value,
        onValueChange = { newText -> text.value = newText },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp)),
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { /* 키보드 숨김 처리 */ }),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFF3F4F6))
                    .fillMaxWidth()
                    .padding(horizontal = 11.dp, vertical = 0.dp), // 가로 패딩만 적용
                contentAlignment = Alignment.Center // Box 내부에서 요소들을 중앙에 배치
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
private fun NumberTextField() {
    val text = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val textStyle = TextStyle(
        color = Color(0xFF171A1F),
        fontSize = 16.sp,
        lineHeight = 50.sp,
        textAlign = TextAlign.Center,
    )

    BasicTextField(
        value = text.value,
        onValueChange = { newText -> text.value = newText },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp)),
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number // 숫자 키보드로 변경
        ),
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide() // 키보드 숨김 처리
        }),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFF3F4F6))
                    .fillMaxWidth()
                    .padding(horizontal = 11.dp, vertical = 0.dp), // 가로 패딩만 적용
                contentAlignment = Alignment.Center // Box 내부에서 요소들을 중앙에 배치
            ) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    innerTextField()
                    Text(text = "Kg", color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        }
    )
}


@Composable
fun AddWeight() {
    Column {
        Text("체중등록", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
        Spacer(modifier = Modifier.height(16.dp))
        NumberTextField()
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(
                    0xFF6D31ED
                )
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "등록",
                fontFamily = Pretend,
                color = Color.White
            )
        }
    }

}

@Composable
fun AddDetailWeight() {
    Column {
        Text("체성분 정보 등록", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
        Spacer(modifier = Modifier.height(16.dp))
        Text("골격근량", fontSize = 12.sp, color = Color(0xff9095A1))
        Spacer(modifier = Modifier.height(7.dp))
        NumberTextField()
        Spacer(modifier = Modifier.height(16.dp))
        Text("체지방량", fontSize = 12.sp, color = Color(0xff9095A1))
        Spacer(modifier = Modifier.height(7.dp))
        NumberTextField()
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(
                    0xFF6D31ED
                )
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "등록",
                fontFamily = Pretend,
                color = Color.White
            )
        }
    }

}


@Composable
fun StopEatEdit() {
    var hour by remember { mutableStateOf(LocalTime.now().hour) }
    var minute by remember { mutableStateOf(LocalTime.now().minute) }
    val fastingHours = remember { mutableStateOf("") }
    val onTimeChanged = { newHour: Int, newMinute: Int ->
        hour = newHour
        minute = newMinute
    }

    Column {
        Text("단식 설정", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
        Spacer(modifier = Modifier.height(30.dp))
        Text("시작 시간 설정", fontSize = 12.sp, color = Color(0xff9095A1))
        TimePicker(hour, minute, onTimeChanged)
        Spacer(modifier = Modifier.height(16.dp))
        FastingHoursInput(fastingHours)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Handle save */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = "등록",
                color = Color.White
            )
        }
    }
}

@Composable
fun FastingHoursInput(fastingHours: MutableState<String>) {
    val textStyle = TextStyle(
        color = Color(0xFF6F7279),
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )

    val localFocusManager = LocalFocusManager.current

    BasicTextField(
        value = fastingHours.value,
        onValueChange = { newText ->
            // Allow the user to clear the input or update it within the range 0-24
            if (newText.isEmpty() || newText.toIntOrNull()?.let { it in 0..24 } == true) {
                fastingHours.value = newText
            }
        },
        singleLine = true,
        textStyle = textStyle,
        modifier = Modifier
            .fillMaxWidth()
            .height(39.dp)
            .clip(RoundedCornerShape(19.5.dp)) // 모서리를 부드럽게 처리
            .background(Color.White, RoundedCornerShape(19.5.dp)) // 배경색을 밝게 처리
            .padding(horizontal = 16.dp), // 좌우 패딩 적용
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number, // 숫자 키보드 사용
            imeAction = ImeAction.Done // 완료 액션 적용
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // 입력한 시간을 검증하고 키보드 포커스 제거
                fastingHours.value.toIntOrNull()?.let {
                    if (it in 0..24) {
                        localFocusManager.clearFocus()
                    }
                }
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp) // 내부 텍스트 필드 패딩
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically // 텍스트 필드 내 요소들을 세로 중앙에 정렬
            ) {
                Text("단식시간 설정", style = textStyle.copy(color = Color.Gray))
                Spacer(modifier = Modifier.weight(1f)) // 이것을 추가하여 "단식시간 설정"과 "시간" 사이의 공간을 최대로 늘림
                Box(
                    contentAlignment = Alignment.CenterEnd, // 텍스트를 오른쪽으로 정렬
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Box가 남은 공간을 채우도록 설정
                ) {
                    innerTextField() // 내부 텍스트 필드 렌더링, 이제 오른쪽 정렬됨
                }
                Text("시간", style = textStyle.copy(color = Color.Gray)) // 레이블 추가
            }
        }

    )
}

@Composable
fun TimePicker(selectedHour: Int, selectedMinute: Int, onTimeChanged: (Int, Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 23
                    value = selectedHour
                    setOnValueChangedListener { _, _, newVal ->
                        onTimeChanged(newVal, selectedMinute)
                    }
                }
            }
        )
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 59
                    value = selectedMinute
                    setOnValueChangedListener { _, _, newVal ->
                        onTimeChanged(selectedHour, newVal)
                    }
                }
            }
        )
    }
}


@Composable
fun StopEatTimer(modifier: Modifier, color: Color, setupAble: Boolean) {
    Box(
        modifier = modifier
            .height(165.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row {
                Text(
                    text = "단식 설정",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    var addSheet by remember {
                        mutableStateOf(false)
                    }
                    if (addSheet) {
                        BottomSheet(closeSheet = { addSheet = false }, { StopEatEdit() })
                    }
                    Image(
                        painter = painterResource(id = R.drawable.gear),
                        contentDescription = "gear",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { addSheet = true }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "14:41:10",
                    color = Color(0xFF37750C),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Text(text = "시작 시간", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "20:00", color = Color(0xff171A1F), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(text = "단식 시간", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "16시간", color = Color(0xff171A1F), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EditWeight(modifier: Modifier, color: Color, setupAble: Boolean) {
    Box(
        modifier = modifier
            .height(165.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(color)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row {
                var addSheet by remember {
                    mutableStateOf(false)
                }
                if (addSheet) {
                    BottomSheet(closeSheet = { addSheet = false }, { AddWeight() })
                }
                Text(
                    text = "체중 등록",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    Image(
                        painter = painterResource(id = R.drawable.gear),
                        contentDescription = "gear",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { addSheet = true }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "70Kg",
                    color = Color(0xFF9C7F00),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
            Row {
                Text(text = "저번달 몸무게", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "68kg", color = Color(0xff171A1F), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(text = "올해 평균", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "72kg", color = Color(0xff171A1F), fontSize = 12.sp)
            }
        }
    }
}