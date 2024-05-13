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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mealtoyou.R
import com.example.mealtoyou.api.SupplementApiService
import com.example.mealtoyou.data.SupplementResponseData
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.viewmodel.FoodSearchViewModel
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
private fun NumberTextField(modifier: Modifier) {
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
    var fields = remember { mutableStateListOf<UUID>() }

    Column {
        Text(text = "영양제 관리", fontSize = 16.sp, color = Color(0xff171A1F), fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(items = fields, key = { it }) { fieldId ->
                Text(text = "이름", fontSize = 14.sp, color = Color(0xff9095A1))
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    NumberTextField(Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.minusred),
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                fields.remove(fieldId)
                            }
                    )
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
                            fields.add(UUID.randomUUID())
                        }
                    }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(13.dp))
        Button(
            onClick = {
                if (fields.size < 3) {
                    fields.add(UUID.randomUUID())
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
    Log.d("ssss","${supplements}")
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