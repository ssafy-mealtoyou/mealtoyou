package com.example.mealtoyou.ui.theme.mypage

import SupplementViewModel
import android.annotation.SuppressLint
import android.util.Log
import android.widget.NumberPicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.R
import com.example.mealtoyou.data.UserHealthInfoUpdateData
import com.example.mealtoyou.data.model.response.UserHealthResDto
import com.example.mealtoyou.handler.HealthEventHandler
import com.example.mealtoyou.handler.UserEventHandler
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.main.stage.Challenge
import com.example.mealtoyou.ui.theme.main.stage.DrugInfo
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.example.mealtoyou.ui.theme.shared.shadowModifier
import com.example.mealtoyou.viewmodel.HealthViewModel
import com.example.mealtoyou.viewmodel.UserHealthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class User2ViewModel : ViewModel() {
    private val user2ApiService = RetrofitClient.user2Instance

    fun fetchData(
        authorization: String,
        requestDto: UserHealthInfoUpdateData,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = user2ApiService.postHealthInfo(authorization, requestDto)
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("_dailyDiets", data.toString())
                    onSuccess()
                } else {
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    Log.e("error", errorMessage)
                }
            } catch (e: Exception) {
                Log.e("error", "2번 에러입니다.")
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MyPage(
    supplementViewModel: SupplementViewModel,
    healthViewModel: HealthViewModel,
    navController: NavHostController
) {
    val userHealthViewModel: UserHealthViewModel = viewModel()
    val userHealthInfo by userHealthViewModel._userHealthResult.collectAsState()
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
//                        val healthData by healthViewModel._bodyResult.collectAsState()
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
                                    text = "${userHealthInfo?.inbodyBoneMuscle ?: 0}kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 24.sp,
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
                                    text = "${userHealthInfo?.inbodyBodyFat ?: 0}kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 24.sp,
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
                                        {
                                            AddDetailWeight(onSuccess = {
                                                addSheet = false
//                                                refreshMyPage = !refreshMyPage
                                                userHealthViewModel.viewModelScope.launch {
                                                    userHealthViewModel.refreshUserHealth()
                                                }
                                            })
                                        })
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
                                    onClick = {
                                        healthEventHandlerState.value?.readHealthData(
                                            healthViewModel,
                                            onSuccess = {
                                                userHealthViewModel.viewModelScope.launch {
                                                    userHealthViewModel.refreshUserHealth()
                                                }
                                            }
                                        )
                                    },
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
                    if (userHealthInfo != null) {
                        StopEatTimer(
                            Modifier.weight(9f),
                            Color(0xFFF1FDE9),
                            true,
                            userHealthInfo!!,
                            userHealthViewModel
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    if (userHealthInfo != null) {
                        EditWeight(
                            Modifier.weight(9f), Color(0xFFFFFAE1), true,
                            userHealthInfo!!, userHealthViewModel
                        )
                    }
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
                    DrugInfo(Modifier.weight(9f), Color(0xFFF0F9FF), true, supplementViewModel)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "로그아웃",
                        fontFamily = Pretend,
                        color = Color(0xFF6D31ED),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable {
                                MainApplication.prefs.removeValue("accessToken")
                                MainApplication.prefs.removeValue("refreshToken")
                                MainApplication.prefs.removeValue("userId")

                                // NavController를 사용하여 메인 화면으로 이동
                                // 이 예제에서는 navController가 이미 설정되어 있고, 사용 가능하다고 가정합니다.
                                navController.navigate("mainPage") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                    launchSingleTop = true // 이미 현재 대상이면 새 인스턴스를 생성하지 않음
                                    restoreState = true // 이전 상태 복원 옵션
                                }
                            }
                            .height(36.dp)
                            .width(158.dp)
                            .padding(vertical = 8.dp),
                    )
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
private fun NumberTextField(text: String, onValueChange: (String) -> Unit) {
//    val text = remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val textStyle = TextStyle(
        color = Color(0xFF171A1F),
        fontSize = 16.sp,
        lineHeight = 50.sp,
        textAlign = TextAlign.Center,
    )

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
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
fun AddWeight(onAddSheetChange: (Boolean) -> Unit, userHealthViewModel: UserHealthViewModel) {
    val weight = remember { mutableStateOf("") }

    Column {
        Text("체중등록", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
        Spacer(modifier = Modifier.height(16.dp))
        NumberTextField(text = weight.value, onValueChange = { newText -> weight.value = newText })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onAddSheetChange(false)
                UserEventHandler().sendUserWeight(weight.value)
                userHealthViewModel.viewModelScope.launch {
                    userHealthViewModel.refreshUserHealth()
                }
            },
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
private fun NumberTextField2(onValueChanged: (String) -> Unit) {
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
        onValueChange = {
            text.value = it
            onValueChanged(it) // 값이 변경될 때마다 전달
        },
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
fun AddDetailWeight(onSuccess: () -> Unit) {
    val viewModel: User2ViewModel = viewModel()
    val (skeletalMuscle, setSkeletalMuscle) = remember { mutableStateOf("") } // 골격근량 값
    val (bodyFat, setBodyFat) = remember { mutableStateOf("") } // 체지방량 값
    Column {
        Text("체성분 정보 등록", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
        Spacer(modifier = Modifier.height(16.dp))
        Text("골격근량", fontSize = 12.sp, color = Color(0xff9095A1))
        Spacer(modifier = Modifier.height(7.dp))
        NumberTextField2(onValueChanged = { value ->
            setSkeletalMuscle(value)
        })
        Spacer(modifier = Modifier.height(16.dp))
        Text("체지방량", fontSize = 12.sp, color = Color(0xff9095A1))
        Spacer(modifier = Modifier.height(7.dp))
        NumberTextField2(onValueChanged = { value ->
            setBodyFat(value)
        })
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // 입력된 값을 UserHealthInfoUpdateData에 넣어서 사용
                val updateData = UserHealthInfoUpdateData(
                    skeletalMuscle = skeletalMuscle.toIntOrNull() ?: 0,
                    bodyFat = bodyFat.toIntOrNull() ?: 0
                )
                viewModel.fetchData(MainApplication.prefs.getValue("accessToken"), updateData) {
                    onSuccess()
                }

            },
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
fun StopEatEdit(
    userHealthInfo: UserHealthResDto,
    onAddSheetChange: (Boolean) -> Unit,
    userHealthViewModel: UserHealthViewModel
) {
    var hour by remember { mutableStateOf(LocalTime.now().hour) }
    var minute by remember { mutableStateOf(LocalTime.now().minute) }
    val fastingHours = remember { mutableStateOf("0") }
    val onTimeChanged = { newHour: Int, newMinute: Int ->
        hour = newHour
        minute = newMinute
    }
    val isFastingEnabled = remember { mutableStateOf(userHealthInfo.intermittentYn) }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("단식 설정", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xff171A1F))
            Spacer(modifier = Modifier.width(8.dp)) // 텍스트와 스위치 사이의 공간
            // 슬라이더 (Switch)
            Switch(
                checked = isFastingEnabled.value,
                onCheckedChange = { isFastingEnabled.value = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF6D31ED),
                    uncheckedThumbColor = Color(0xFFBDBDBD)
                )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text("시작 시간 설정", fontSize = 12.sp, color = Color(0xff9095A1))
        TimePicker(hour, minute, onTimeChanged)
        Spacer(modifier = Modifier.height(16.dp))
        FastingHoursInput(fastingHours)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onAddSheetChange(false)
                val endHour = (hour + fastingHours.value.toInt()) % 24
                Log.d("switch 상태", isFastingEnabled.value.toString())
                Log.d("시간", "$endHour:$minute")
                Log.d("시작끝", endHour.toString())
                UserEventHandler().sendUserIntermittent(
                    isFastingEnabled.value.toString(),
                    String.format("%02d:%02d", hour, minute),
                    String.format("%02d:%02d", endHour, minute)
                )
                userHealthViewModel.viewModelScope.launch {
                    userHealthViewModel.refreshUserHealth()
                }
            },
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


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun StopEatTimer(
    modifier: Modifier,
    color: Color,
    setupAble: Boolean,
    userHealthInfo: UserHealthResDto,
    userHealthViewModel: UserHealthViewModel
) {

    var remainingSeconds by remember { mutableStateOf(0) }

    Log.d("남은시간",userHealthInfo.intermittentYn.toString())
    // LaunchedEffect를 사용하여 타이머 로직을 구현합니다.
    LaunchedEffect(key1 = userHealthInfo.intermittentYn) {
        if (userHealthInfo.intermittentYn) {
            val startTime = LocalTime.parse(userHealthInfo.intermittentStartTime).toSecondOfDay()
            val endTime = LocalTime.parse(userHealthInfo.intermittentEndTime).toSecondOfDay()
            val currentTime = LocalTime.now().toSecondOfDay()
            // 종료 시간이 시작 시간보다 작은 경우, 즉 자정을 넘어가는 경우
            val adjustedEndTime = if (endTime <= startTime) endTime + 24 * 3600 else endTime
            val adjustedCurrentTime =
                if (currentTime < startTime) currentTime + 24 * 3600 else currentTime

            // 종료 시간까지 남은 시간 계산
            val remaining = adjustedEndTime - adjustedCurrentTime
            Log.d("단식 남은 시간", remaining.toString())
            // 남은 시간이 양수인 경우
            if (endTime != startTime && remaining > 0) {
                remainingSeconds = remaining
            }
        }

        while (remainingSeconds > 0) {
            delay(1000) // 1초 기다립니다.
            remainingSeconds-- // 남은 시간을 1초 줄입니다.
        }
    }
    // 시간을 HH:MM:SS 형식으로 변환합니다.
    val hours = TimeUnit.SECONDS.toHours(remainingSeconds.toLong())
    val minutes = TimeUnit.SECONDS.toMinutes(remainingSeconds.toLong()) % 60
    val seconds = remainingSeconds % 60
    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)
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
                    val onAddSheetChange = { value: Boolean ->
                        addSheet = value
                    }
                    if (addSheet) {
                        BottomSheet(
                            closeSheet = { addSheet = false },
                            { StopEatEdit(userHealthInfo, onAddSheetChange, userHealthViewModel) })
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
                    text = timeText,
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
                Text(
                    text = if (userHealthInfo.intermittentYn) {
                        userHealthInfo.intermittentStartTime.split(":").take(2)
                            .joinToString(":")
                    } else "00:00", color = Color(0xff171A1F), fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(text = "단식 시간", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (userHealthInfo.intermittentYn) {
                        calculate(
                            userHealthInfo.intermittentStartTime,
                            userHealthInfo.intermittentEndTime
                        )
                    } else "0 시간", color = Color(0xff171A1F), fontSize = 12.sp
                )
            }
        }
    }
}

fun calculate(start: String, end: String): String {
    val startHour = start.split(":")[0].toInt()
    val endHour = end.split(":")[0].toInt()
    val difference = if (endHour >= startHour) {
        endHour - startHour
    } else {
        (24 - startHour) + endHour
    }

    return "$difference 시간"
}

@Composable
fun EditWeight(
    modifier: Modifier,
    color: Color,
    setupAble: Boolean,
    userHealthInfo: UserHealthResDto,
    userHealthViewModel: UserHealthViewModel
) {
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
                val onAddSheetChange = { value: Boolean ->
                    addSheet = value
                }
                if (addSheet) {
                    BottomSheet(
                        closeSheet = { addSheet = false },
                        { AddWeight(onAddSheetChange, userHealthViewModel) })
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
                    text = "${userHealthInfo.weight}Kg",
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
                Text(
                    text = "${userHealthInfo.weightLastMonth}kg",
                    color = Color(0xff171A1F),
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Text(text = "올해 평균", color = Color(0xff9095A1), fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = String.format("%.1fkg", userHealthInfo.weightThisYear),
                    color = Color(0xff171A1F),
                    fontSize = 12.sp
                )
            }
        }
    }
}