package com.example.mealtoyou.ui.theme.group

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.InfoRow
import com.example.mealtoyou.ui.theme.shared.MainBar

@Composable
fun GroupPage(navController: NavHostController) {
    val mode = remember { mutableStateOf("search") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = constraints.maxWidth.toFloat()

            AnimatedVisibility(
                visible = mode.value == "search",
                enter = slideIn(
                    initialOffset = { IntOffset(screenWidth.toInt(), 0) },
                    animationSpec = tween(300)
                ),
                exit = slideOut(
                    targetOffset = { IntOffset(-screenWidth.toInt(), 0) },
                    animationSpec = tween(300)
                )
            ) {

                SearchScreen { mode.value = "detail" }
            }

            AnimatedVisibility(
                visible = mode.value == "detail",
                enter = slideIn(
                    initialOffset = { IntOffset(screenWidth.toInt(), 0) },
                    animationSpec = tween(300)
                ),
                exit = slideOut(
                    targetOffset = { IntOffset(-screenWidth.toInt(), 0) },
                    animationSpec = tween(300)
                )
            ) {

                DetailScreen("MODE") { navController.navigate("chat") }
            }
        }
    }
}

@Composable
private fun CustomTextField() {
    val text = remember { mutableStateOf("") }
    val textStyle = TextStyle(
        color = Color(0xFF6F7279),
        fontSize = 14.sp  // 폰트 크기는 적절히 조절하세요.
    )

    BasicTextField(
        value = text.value,
        onValueChange = { newText -> text.value = newText },
        singleLine = true,
        textStyle = textStyle,
        modifier = Modifier
            .width(269.dp)
            .height(39.dp)
            .clip(RoundedCornerShape(19.5.dp)),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { /* 키보드 숨김 처리 */ }),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color(0xFFF3F4F6))
                    .padding(11.dp)  // 여기서는 모든 외부 패딩을 제거합니다
            ) {
                innerTextField()
            }
        }
    )
}

@Composable
fun TextSample() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.ava),
            contentDescription = "Icon",
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Box(
                modifier = Modifier
                    .width(284.dp)
                    .wrapContentWidth(Alignment.Start)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                    .background(Color(0xFFD3C1FA))
                    .padding(top = 3.dp, start = 10.dp, end = 10.dp)
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = "Ipsum reprehenderit ea nulla velit dolore laborum in id sint tempor et magna tempor veniam. Pariatur cillum venia dolore",
                    color = Color(0xFF171A1F),
                    lineHeight = 22.sp
                )
            }
            Text(
                text = "2 mins ago",
                color = Color(0xFF9095A1),
                fontSize = 12.sp,
                lineHeight = 30.sp
            )
        }
    }
}

@Composable
fun TextSample2() {
    Row(
        modifier = Modifier.fillMaxWidth(),  // Row가 최대 너비를 차지하도록 설정
        horizontalArrangement = Arrangement.End  // 내용을 오른쪽으로 정렬
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Column(Modifier.wrapContentWidth()) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .width(284.dp)
                        .wrapContentWidth(Alignment.End)  // Box를 오른쪽 정렬
                        .clip(
                            RoundedCornerShape(
                                topStart = 8.dp,
                                topEnd = 0.dp,
                                bottomEnd = 8.dp,
                                bottomStart = 8.dp
                            )
                        )
                        .background(Color(0xFFF3F4F6))
                        .padding(top = 3.dp, start = 10.dp, end = 10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(10.dp),
                        text = "식단 공유합니다.",
                        color = Color(0xFF171A1F),
                        lineHeight = 22.sp
                    )
                }

            }
            DietBox()
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "2 mins ago",
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                    lineHeight = 30.sp
                )
            }

        }
    }
}

@Composable
fun ChatScreen() {
    val infoModifier = Modifier
        .defaultShadow()
        .height(61.dp)
        .fillMaxWidth()
        .padding(top = 1.dp)
        .background(Color.White)

    val scroll = rememberScrollState()
    val messageCount = remember { mutableIntStateOf(10) } // 메시지 수를 추적하기 위한 상태
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                // 포커스 해제로 키보드를 숨깁니다
                focusManager.clearFocus()
            }),
        color = Color.White
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MainBar(text = "그룹 채팅", infoImg = true)

            Column(
                Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxHeight(0.915f)
                    .verticalScroll(scroll)
            ) {
                // 예시로 TextSample 컴포저블을 여러 번 호출
                repeat(messageCount.intValue) {
                    TextSample()
                    Spacer(modifier = Modifier.height(8.dp))
                    TextSample2()
                }
            }

            // 스크롤을 맨 아래로 이동
            LaunchedEffect(messageCount.intValue) {
                scroll.scrollTo(scroll.maxValue)
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = infoModifier) {
                Row {
                    Box(modifier = Modifier.padding(12.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Icon",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        CustomTextField()
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Image(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Send Icon",
                        modifier = Modifier.size(52.dp)
                    )
                }
            }
        }
    }

}

@Composable
private fun SearchScreen(function: () -> Unit) {
    val infoModifier = Modifier
        .height(108.dp)
        .fillMaxWidth()
        .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        .defaultShadow()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F1FE))
        .clickable { function.invoke() }

    Column {
        MainBar(text = "그룹", infoImg = true)
        Box(modifier = infoModifier) {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "다이어트 모임",
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                    color = Color(0xFF171A1F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                InfoRow()
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = infoModifier) {
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "다이어트 모임",
                    modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                    color = Color(0xFF171A1F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                InfoRow()
            }

        }
    }

}

@Composable
private fun DetailScreen(name: String, function: () -> Unit) {
    val scrollState = rememberScrollState()
    Column {
        MainBar(text = name, infoImg = true)
        Column(modifier = Modifier
            .verticalScroll(scrollState)
            .clickable { function.invoke() }) {

            InfoSection()
            ContentRows()
            Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                DietBox()
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
private fun InfoSection() {
    val infoModifier = Modifier
        .height(66.dp)
        .fillMaxWidth()
        .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        .defaultShadow()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F1FE))

    Box(modifier = infoModifier) {
        InfoRow()
    }
}

@Composable
private fun ContentRows() {
    Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
        ContentBox(Modifier.weight(1f)) { FirstContent() }
        Spacer(modifier = Modifier.width(16.dp))
        ContentBox(Modifier.weight(1f)) { SecondContent() }
    }
}

@Composable
private fun FirstContent() {
    Column {
        Text("일일 목표", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        Text(
            "이번 주는 3회 남았어요.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF323743)
        )
        Spacer(modifier = Modifier.weight(1f))
        Row {
            Column {
                Text("걸음 수", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text(
                    "9,281 걸음",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF323743)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text("소모 칼로리", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text(
                    "428 kcal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF323743)
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6D31ED)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .height(36.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = true,
                    ambientColor = Color(0xFF171A1F).copy(alpha = 0.15f),
                    spotColor = Color(0xFF171A1F).copy(alpha = 0.15f)
                ),
        ) {
            Text(text = "일일 목표 인증", fontFamily = Pretend, color = Color.White)
        }
    }
}

@Composable
private fun SecondContent() {
    Column {
        Text("그룹 채팅", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(
                text = "일일 목표 인증",
                color = Color(0xFF323743),
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(
                text = "쿠쿠하세요",
                color = Color(0xFF323743),
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(32.dp)
                .wrapContentWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(Color(0xFFF3F4F6))
                .padding(top = 3.dp, start = 10.dp, end = 10.dp)
        ) {
            Text(
                text = "신박하네요?",
                color = Color(0xFF323743),
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}


@Composable
private fun ContentBox(modifier: Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .height(166.dp)
            .defaultShadow()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        content()
    }
}

private fun Modifier.defaultShadow() = this.shadow(
    elevation = 2.dp,
    shape = RoundedCornerShape(8.dp),
    ambientColor = Color(0xFF171A1F),
    spotColor = Color(0xFF171A1F)
)


