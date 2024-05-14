package com.example.mealtoyou.ui.theme.main.stage

import SupplementViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.api.SupplementApiService
import com.example.mealtoyou.data.SupplementInfo
import com.example.mealtoyou.data.SupplementRequestData
import com.example.mealtoyou.data.repository.SupplementRepository.registerSupplements
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Composable
fun Drug(drug: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = drug),
            contentDescription = "drug Icon",
            modifier = Modifier.size(45.dp)
        )
        Text(
            text = "비타민",
            color = Color(0xFF323743),
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 20.sp,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun NumberTextField(modifier: Modifier,  text: MutableState<String>) {
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
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(8.dp)),
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
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
                innerTextField()
            }
        }
    )
}

@Composable
fun AddDrug() {
    val coroutineScope = rememberCoroutineScope()  // 코루틴 스코프 생성
//    var fields = remember { mutableStateListOf<UUID>() }
    var fields = remember { mutableStateListOf<SupplementInfo>() }
    val text1 = remember { mutableStateOf("") }
    val text2 = remember { mutableStateOf("") }
    val text3 = remember { mutableStateOf("") }

    Column {
        Text(text = "영양제 관리", fontSize = 16.sp, color = Color(0xff171A1F), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(items = fields, key = { it.id }) { supplementInfo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        Modifier
                            .weight(4.5f)
                            .wrapContentHeight()) {
                        Text(text = "이름", fontSize = 14.sp, color = Color(0xff9095A1))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            val text = remember { mutableStateOf("") }
                            TextField(
                                value = supplementInfo.name.value,
                                onValueChange = { supplementInfo.name.value = it },
                                Modifier.weight(4.5f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        Modifier
                            .weight(1f)
                            .wrapContentHeight()) {
                        Text(text = "시", fontSize = 14.sp, color = Color(0xff9095A1))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            val text = remember { mutableStateOf("") }
                            TextField(value = supplementInfo.hour.value,
                                onValueChange = { supplementInfo.hour.value = it },
                                Modifier.weight(4.5f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        Modifier
                            .weight(1f)
                            .wrapContentHeight()) {
                        Text(text = "분", fontSize = 14.sp, color = Color(0xff9095A1))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row {
                            val text = remember { mutableStateOf("") }
                            TextField(
                                value = supplementInfo.minute.value,
                                onValueChange = { supplementInfo.minute.value = it },
                                Modifier.weight(4.5f),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ))
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Spacer(modifier = Modifier.height(18.dp))
                        Image(
                            painter = painterResource(id = R.drawable.minusred),
                            contentDescription = "Google Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    fields.remove(supplementInfo)
                                }
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.pluspupple),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (fields.size < 3) {
                            fields.add(SupplementInfo())
//                            fields.add(UUID.randomUUID())
                        }
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(13.dp))
        Button(
            onClick = {
                coroutineScope.launch {  // 코루틴 내에서 비동기 함수 호출
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                    val dataList = fields.map { supplementInfo ->
//                        val hour = supplementInfo.hour.value.toIntOrNull() ?: 0
//                        val minute = supplementInfo.minute.value.toIntOrNull() ?: 0
//                        val alertTime = LocalTime.of(hour, minute).format(timeFormatter)
                        val hourInt = supplementInfo.hour.value.toIntOrNull() ?: 0  // null일 경우 0을 기본값으로 사용
                        val minuteInt = supplementInfo.minute.value.toIntOrNull() ?: 0
                        val alertTime = LocalTime.of(hourInt, minuteInt)
                        Log.d("alertTime","${alertTime}")
                        SupplementRequestData(
                            name = supplementInfo.name.value,
                            takenYn = false,
                            alertTime = alertTime
                        )
                    }
                    registerSupplements(dataList)
                    // 결과에 따라 UI를 업데이트하거나 사용자에게 알림
                }
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
            modifier = Modifier
                .height(46.dp)
                .fillMaxWidth(),
        ) {
            Text("등록", color = Color.White)
        }
    }
}


@Composable
fun DrugInfo(
    modifier: Modifier,
    color: Color,
    setupAble: Boolean,
    supplementViewModel : SupplementViewModel
) {
//    val viewModel: SupplementViewModel = viewModel()

    val supplements = supplementViewModel.supplementResult.collectAsState().value
//    Log.d("ssss","${supplements}")
//    LaunchedEffect(key1 = true) {
//        supplementViewModel.supplementScreen()
//    }

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
                    text = "영양제",
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
                        BottomSheet(closeSheet = { addSheet = false }, { AddDrug() })
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
            if (supplements.isNullOrEmpty()) {
                Text("영양제 데이터가 없습니다.")
            } else {
                // 완료율 계산 예제: 총 영양제 중에서 takenYn이 true인 영양제의 비율을 표시
                Log.d("rate", "${supplements.count { it.takenYn }} + ${supplements.size}")
                val completionRate =
                    (supplements.count { it.takenYn }.toDouble() / supplements.size) * 100
                Log.d("test", "${(supplements.count { it.takenYn }.toDouble() / supplements.size)}")
                Log.d("c", "${completionRate}")
                Text(
                    text = "${completionRate.toInt()}% 완료",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 26.sp,
                    fontSize = 16.sp,
                    color = Color(0xFF323743)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            val displayedDrugs = supplements?.take(3)

            Row() {
                if (displayedDrugs != null) {
                    displayedDrugs.forEach { drugData ->
                        Spacer(modifier = Modifier.weight(1f))
                        Drug(drug = if (drugData.takenYn) R.drawable.used_drug else R.drawable.unused_drug)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}