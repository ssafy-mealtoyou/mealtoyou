package com.example.mealtoyou.ui.theme.main.stage

import android.util.Log
import android.widget.NumberPicker
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealtoyou.R
import com.example.mealtoyou.data.UserGoalRequestData
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.CircleProgressBar
import com.example.mealtoyou.ui.theme.shared.VerticalProgressBar
import com.example.mealtoyou.viewmodel.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Calendar

@Composable
fun WeightProgress(value: Float, text: String, centerText: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircleProgressBar(value, 80.dp, centerText)
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontFamily = Pretend,
            lineHeight = 20.sp,
            fontSize = 12.sp,
            color = Color(0xFF323743)
        )
    }
}


@Composable
fun DatePicker(
    selectedDay: Int,
    selectedMonth: Int,
    selectedYear: Int,
    onDateChanged: (Int, Int, Int) -> Unit
) {
    val months =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // Day
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = 1
                    maxValue =
                        31  // Simplification for example; real implementation should consider the selected month/year
                    value = selectedDay
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(newVal, selectedMonth, selectedYear)
                    }
                }
            }
        )

        // Month
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = 0
                    maxValue = 11
                    displayedValues = months.toTypedArray()
                    value = selectedMonth
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(selectedDay, newVal, selectedYear)
                    }
                }
            }
        )

        // Year
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = 1900
                    maxValue = 2100
                    value = selectedYear
                    setOnValueChangedListener { _, _, newVal ->
                        onDateChanged(selectedDay, selectedMonth, newVal)
                    }
                }
            }
        )
    }
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
                    .padding(horizontal = 16.dp, vertical = 11.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()  // Row를 Box의 높이만큼 채우도록 설정
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    innerTextField()
                    Spacer(modifier = Modifier.width(8.dp))  // 'innerTextField'와 'kg' 텍스트 사이의 간격 추가
                    Text(text = "kg", color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    )
}

@Composable
fun ChallengeSetup() {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var goalWeight by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.of(currentYear, currentMonth + 1, currentDay).toString()) }

    Column {
        Text(text = "목표 몸무게", color = Color(0xFF9095A1))
        Spacer(modifier = Modifier.height(12.dp))
        TextField(
            value = goalWeight,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    goalWeight = it
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "기간 설정", color = Color(0xFF9095A1))
        DatePicker(
            selectedDay = currentDay,
            selectedMonth = currentMonth,
            selectedYear = currentYear
        ) { day, month, year ->
            selectedDate = LocalDate.of(year, month + 1, day).toString()
            Log.d("DatePicker", "Selected date: $year-${month + 1}-$day")
        }
        Button(
            onClick = {
                val goalWeightValue = goalWeight.toIntOrNull()
                if (goalWeightValue != null) {
                    val requestDto = UserGoalRequestData(goalWeightValue, selectedDate)
                    RetrofitClient.userInstance.updateGoal(requestDto).enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            if (response.isSuccessful) {
                                Log.d("API", "Goal updated successfully")
                                // 성공 시 처리
                            } else {
                                Log.e("API", "Failed to update goal")
                                // 실패 시 처리
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("API", "Error: ${t.message}")
                            // 오류 처리
                        }
                    })
                } else {
                    Log.e("Input", "Invalid goal weight")
                    // 잘못된 입력 처리
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
fun Challenge(modifier: Modifier, color: Color, setupAble: Boolean, userViewModel: UserViewModel = viewModel()) {

    val userHomeResponse = userViewModel.userHomeResponse

    LaunchedEffect(true) {
        userViewModel.updateUserHome()
    }

    fun getUserGoalWeightPer(): Float {
        val ans: Float
        val diffStart = userHomeResponse.goalWeight - userHomeResponse.goalStartWeight
        val diffCurrent = userHomeResponse.currentWeight - userHomeResponse.goalStartWeight
        ans = (diffCurrent / diffStart).toFloat()
        if (ans.isNaN())
            return 0f
        return ans.coerceIn(0.0f, 1.0f)
    }

    fun getUserGoalDateDiff(): Int {
        if (userHomeResponse.goalStartDate == "" || userHomeResponse.goalEndDate == "") return 0
        return try {
            val today = LocalDate.now()
            val endDate = LocalDate.parse(userHomeResponse.goalEndDate)

            if (today > endDate) 0 else ChronoUnit.DAYS.between(today, endDate).toInt()
        } catch (e: Exception) {
            // 예외 발생 시 로그를 남기고 0을 반환
            e.printStackTrace()
            0
        }
    }

    fun getUserRemainingGoalDatePercentage(): Float {
        if (userHomeResponse.goalStartDate == "" || userHomeResponse.goalEndDate == "") return 0f

        try {
            val startDate = LocalDate.parse(userHomeResponse.goalStartDate)
            val endDate = LocalDate.parse(userHomeResponse.goalEndDate)
            val today = LocalDate.now()

            // 시작 날짜가 끝 날짜나 현재 날짜보다 후면 0을 반환
            if (startDate > endDate || startDate > today) return 0f

            // 전체 기간과 남은 기간을 계산
            val totalDays = ChronoUnit.DAYS.between(startDate, endDate).toFloat()
            val remainingDays = ChronoUnit.DAYS.between(today, endDate).toFloat()

            // 남은 기간의 비율을 계산하여 반환
            return if (totalDays == 0f) 0f else 1.0f - remainingDays / totalDays
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0f
    }

    val userGoalWeightPer = getUserGoalWeightPer()
    val userRemainingGoalDate = getUserGoalDateDiff()
    val userRemainingGoalDatePer = getUserRemainingGoalDatePercentage()

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
                    text = if (!setupAble) "목표 달성치" else "목표 설정",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    fontSize = 12.sp,
                    color = Color(0xFF9095A1)
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    var addSheet by remember {
                        mutableStateOf(false)
                    }
                    if (addSheet) {
                        BottomSheet(closeSheet = { addSheet = false }, { ChallengeSetup() })
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
                WeightProgress(value = userGoalWeightPer, "몸무게", "${userHomeResponse.currentWeight.toInt()}kg")
                Spacer(modifier = Modifier.width(24.dp))
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    VerticalProgressBar(
                        progress = userRemainingGoalDatePer,
                        color = Color(0xFF6D31ED),
                        width = 29.dp,
                        height = 80.dp,
                        bgColor = if (setupAble) Color(0xFFCCCCCC) else Color(0xFFF3F4FF)
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                    Text(
                        text = "D-$userRemainingGoalDate",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Pretend,
                        lineHeight = 20.sp,
                        fontSize = 12.sp,
                        color = Color(0xFF323743)
                    )
                }
            }

        }
    }
}
