package com.example.mealtoyou.ui.theme.group

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.mealtoyou.R
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.diet.DailyDietsResponseDto
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.diet.Diet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel : ViewModel() {
    private val chatApiService = RetrofitClient.chatInstance

    // 이미지 URL을 저장하는 StateMap
    val imageUrls = mutableStateMapOf<Int, String>()
    val userNames = mutableStateMapOf<Int, String>()

    fun loadUserImages(userId: Int) {
        if (!imageUrls.containsKey(userId)) {
            imageUrls[userId] = "Loading..."  // 초기 로딩 상태 설정
            viewModelScope.launch {
                try {
                    val response = chatApiService.getUserImage(userId)
                    if (response.isSuccessful) {
                        imageUrls[userId] =
                            response.body()?.get(0)?.profileImageUrl ?: "No image found"

                        userNames[userId] =
                            response.body()?.get(0)?.nickname ?: "No image found"
                    } else {
                        imageUrls[userId] = "Error fetching image"
                        userNames[userId] = "Error fetching user"
                    }
                } catch (e: Exception) {
                    imageUrls[userId] = "Network request failed"
                }
            }
        }
    }

    private val _diets = mutableStateOf<DailyDietsResponseDto?>(null)
    val diets: State<DailyDietsResponseDto?> = _diets
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loadUserDiets() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response =
                    chatApiService.getUserDiets("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDNW9LaVlLTlJYTCtJNWhvTEJsUW5nPT0iLCJpYXQiOjE3MTU0ODI1NDAsImV4cCI6MTcyMzI1ODU0MH0.xjix3Z-xEogbiBjD0CNTVUXLmPdmns2NgX5DIcx5fqs")
                if (response.isSuccessful) {
                    _diets.value = response.body()
                    Log.d("API Success", response.message())
                    _isLoading.value = false
                } else {
                    Log.e("API Error", "Failed to load diets: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API Error", "Exception during fetching diets: $e")
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen() {
    val infoModifier = Modifier
        .defaultShadow()
        .height(61.dp)
        .fillMaxWidth()
        .padding(top = 1.dp)
        .background(Color.White)
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val messageList = remember { mutableStateListOf<String>() }
    val messageToSend = remember { mutableStateOf("") }
    val chatViewModel: ChatViewModel = viewModel()

    val webSocketClient = remember {
        WebSocketClient(
            "wss://a102.mgbg.kr/api/chatting-service/chat/group:11",
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDNW9LaVlLTlJYTCtJNWhvTEJsUW5nPT0iLCJpYXQiOjE3MTU0ODI1NDAsImV4cCI6MTcyMzI1ODU0MH0.xjix3Z-xEogbiBjD0CNTVUXLmPdmns2NgX5DIcx5fqs"
        ) { message ->
            scope.launch {
                messageList.add(message)
            }
        }
    }

    val uid = 4
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


                messageList.forEach { message ->
                    val chatMessage = parseJsonChatMessage(message)
                    val imageUrl = chatViewModel.imageUrls[chatMessage.userId] ?: "Loading"
                    val userName = chatViewModel.userNames[chatMessage.userId] ?: "Loading"

                    if (imageUrl == "Loading") {
                        LaunchedEffect(chatMessage.userId) {
                            chatViewModel.loadUserImages(chatMessage.userId)
                        }
                    }
                    if (chatMessage.message.message != null) {
                        if (chatMessage.userId != uid) {
                            UserText(
                                chatMessage.message.message,
                                chatMessage.timestamp,
                                imageUrl,
                                userName
                            )
                        } else {
                            MyText(chatMessage.message.message, chatMessage.timestamp)
                        }
                    } else {
                        if (chatMessage.userId != uid) {
                            Row {
                                Image(
                                    painter = rememberImagePainter(
                                        data = imageUrl,
                                        builder = {
                                            crossfade(true)
                                            placeholder(R.drawable.ava)
                                            error(R.drawable.ava)
                                        }
                                    ),
                                    contentDescription = "Icon",
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = userName,
                                    color = Color.Black,
                                    lineHeight = 30.sp,
                                    fontSize = 14.sp
                                )
                            }


                        }
                        val count: Int = chatMessage.message.dailyDietsResponseDto?.diets?.size ?: 0
                        val pagerState = rememberPagerState(
                            pageCount = { count }  // 총 페이지 수
                        )
                        DietBox(
                            chatMessage.message.dailyDietsResponseDto?.diets,
                            pagerState = pagerState
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Log.d("image", imageUrl)
                }
            }

            // 스크롤을 맨 아래로 이동
            LaunchedEffect(messageList.size) {
                scroll.scrollTo(scroll.maxValue)
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(modifier = infoModifier) {
                Row {
                    var addSheet by remember {
                        mutableStateOf(false)
                    }
                    if (addSheet) {
                        BottomSheet(closeSheet = { addSheet = false }, {
                            SelectDiet { data ->
                                run {
                                    addSheet = false
                                    webSocketClient.send(data)
                                    messageList.add(createDietMessage(data, uid))
                                }

                            }
                        })
                    }

                    Box(modifier = Modifier
                        .padding(12.dp)
                        .clickable { addSheet = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.image),
                            contentDescription = "Icon",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        CustomTextField(messageToSend)
                        Spacer(modifier = Modifier.weight(1f))
                    }


                    Image(
                        painter = painterResource(id = R.drawable.send),
                        contentDescription = "Send Icon",
                        modifier = Modifier
                            .size(52.dp)
                            .clickable {
                                messageList.add(createChatMessage(messageToSend.value, uid))
                                webSocketClient.send(
                                    "{\n" +
                                            "    \"type\": \"chat\",\n" +
                                            "    \"message\": \"" + messageToSend.value + "\"\n" +
                                            "}"
                                )
                                messageToSend.value = ""
                            }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectDiet(function: (message: String) -> Unit) {
    val chatViewModel: ChatViewModel = viewModel()
    LaunchedEffect(chatViewModel) {
        chatViewModel.loadUserDiets()
    }

    val isLoading = chatViewModel.isLoading.collectAsState()
    val diets = chatViewModel.diets

    if (!isLoading.value) {
        Text(text = "식단 목록", color = Color.Black, fontWeight = FontWeight.SemiBold)
        val count: Int = diets.value?.diets?.size ?: 0
        val pagerState = rememberPagerState(
            pageCount = { count }  // 총 페이지 수
        )
        DietBox(diet = diets.value?.diets, pagerState = pagerState)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                val dietList: ArrayList<Diet> = ArrayList()
                diets.value?.diets?.get(pagerState.currentPage)?.let { dietList.add(it) }
                val dailyDietsResponseDto = DailyDietsResponseDto(
                    type = "diet",
                    dailyCaloriesBurned = null,
                    diets = dietList,
                    dailyCarbohydrateTaked = null,
                    dailyFatTaked = null,
                    dailyProteinTaked = null,
                    date = null
                )
                val sendMessage = SendMessage(
                    type = "diet",
                    dailyDietsResponseDto = dailyDietsResponseDto
                )
                val gson = Gson()
                val message: String = gson.toJson(sendMessage)
                Log.d("TEST", message)
                function.invoke(message)
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
                text = "식단 공유하기",
                fontFamily = Pretend,
                color = Color.White
            )
        }
    } else {
        Text(text = "로딩중")
    }

}

fun createChatMessage(value: String, userId: Int): String {
    val gson = Gson()
    // Id 객체 생성
    val id = Id(
        timestamp = System.currentTimeMillis(),
        date = "2024-05-09"
    )

    // Message 객체 생성
    val message = Message(
        message = value,
        dailyDietsResponseDto = null
    )

    // ChatMessage 객체 생성
    val chatMessage = ChatMessage(
        id = id,
        userId = userId,
        groupId = 6,
        message = message,
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )

    return gson.toJson(chatMessage)
}

fun createDietMessage(value: String, userId: Int): String {
    val gson = Gson()
    val data = gson.fromJson(value, SendMessage::class.java)
    // Id 객체 생성
    val id = Id(
        timestamp = System.currentTimeMillis(),
        date = "2024-05-09"
    )

    // Message 객체 생성
    val message = Message(
        message = null,
        dailyDietsResponseDto = data.dailyDietsResponseDto
    )

    // ChatMessage 객체 생성
    val chatMessage = ChatMessage(
        id = id,
        userId = userId,
        groupId = 6,
        message = message,
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )

    return gson.toJson(chatMessage)
}

fun parseJsonChatMessage(jsonString: String): ChatMessage {
    val gson = Gson()
    return gson.fromJson(jsonString, ChatMessage::class.java)
}

fun getTimeAgo(timestamp: String): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val messageTime = LocalDateTime.parse(timestamp, formatter)
    val now = LocalDateTime.now(ZoneId.of("UTC"))
    val minutesAgo = ChronoUnit.MINUTES.between(messageTime, now)
    val hoursAgo = ChronoUnit.HOURS.between(messageTime, now)
    val daysAgo = ChronoUnit.DAYS.between(messageTime, now)

    return when {
        minutesAgo < 1 -> "now"
        minutesAgo < 60 -> "$minutesAgo mins ago"
        hoursAgo < 24 -> "$hoursAgo hours ago"
        else -> "$daysAgo days ago"
    }
}

@Composable
fun UserText(message: String, timestamp: String, imageUrl: String, userName: String) {
    var timeAgo by remember { mutableStateOf(getTimeAgo(timestamp)) }

    LaunchedEffect(key1 = timestamp) {
        while (true) {
            timeAgo = getTimeAgo(timestamp)  // 시간 업데이트
            delay(1000)  // 1초 마다 업데이트
        }
    }

    Row {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.ava)
                    error(R.drawable.ava)
                }
            ),
            contentDescription = "Icon",
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        )


        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = userName, color = Color.Black, lineHeight = 28.sp, fontSize = 14.sp)
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
                    text = message,
                    color = Color(0xFF171A1F),
                    lineHeight = 22.sp
                )
            }
            Text(
                text = timeAgo,
                color = Color(0xFF9095A1),
                fontSize = 12.sp,
                lineHeight = 30.sp
            )
        }
    }
}

@Composable
fun MyText(message: String, timestamp: String) {
    var timeAgo by remember { mutableStateOf(getTimeAgo(timestamp)) }

    LaunchedEffect(key1 = timestamp) {
        while (true) {
            timeAgo = getTimeAgo(timestamp)  // 시간 업데이트
            delay(1000)  // 1초 마다 업데이트
        }
    }

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
                        text = message,
                        color = Color(0xFF171A1F),
                        lineHeight = 22.sp
                    )
                }

            }
            // DietBox()
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = timeAgo,
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                    lineHeight = 30.sp
                )
            }

        }
    }
}


@Composable
private fun CustomTextField(text: MutableState<String>) {
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