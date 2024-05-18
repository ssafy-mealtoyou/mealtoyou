package com.example.mealtoyou.ui.theme.group

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.data.CommunityData
import com.example.mealtoyou.data.UserCommunityData
import com.example.mealtoyou.handler.HealthEventHandler
import com.example.mealtoyou.retrofit.RetrofitClient
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.shared.BottomSheet
import com.example.mealtoyou.ui.theme.shared.DietBox
import com.example.mealtoyou.ui.theme.shared.InfoRow
import com.example.mealtoyou.ui.theme.shared.MainBar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CommunityViewModel : ViewModel() {
    private val communityApiService = RetrofitClient.communityInstance

    private val _userCommunityData = MutableStateFlow<UserCommunityData?>(null)
    val userCommunityData: StateFlow<UserCommunityData?> = _userCommunityData

    fun fetchData(authorization: String) {
        viewModelScope.launch {
            try {
                val response = communityApiService.getUserCommunityInfo()
                if (response.isSuccessful) {
                    val data = response.body()
                    _userCommunityData.value = data
                    Log.d("TEST", data.toString())
                } else {
                    // 실패한 경우에 대한 처리
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    // 에러 메시지 처리
                }
            } catch (e: HttpException) {
                // HTTP 예외 처리 (예: 404, 500 등)
                val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
                // 에러 메시지 처리
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                val errorMessage = "Error: ${e.message}"
                // 에러 메시지 처리
            }
        }
    }
}

class CommunityAllViewModel : ViewModel() {
    private val communityApiService = RetrofitClient.communityInstance

    private val _allCommunityData = MutableStateFlow(emptyList<CommunityData>())
    val allCommunityData: StateFlow<List<CommunityData>?> = _allCommunityData

    private val _navigateToDetailScreen = MutableStateFlow<Boolean?>(false)
    val navigateToDetailScreen: StateFlow<Boolean?> = _navigateToDetailScreen

    private val _communityStatus = MutableStateFlow<String?>(null)
    val communityStatus: StateFlow<String?> = _communityStatus

    private val _dailyGoalCheck = MutableStateFlow<String?>(null)
    val dailyGoalCheck: StateFlow<String?> = _dailyGoalCheck

    fun dailyGoalCheck(steps: Int, caloriesBurned: Int): Unit {
        viewModelScope.launch {
            try {
                _dailyGoalCheck.value = "loading"
                val response =
                    communityApiService.dailyGoalCheck(
                        MainApplication.prefs.getValue("accessToken"),
                        steps, caloriesBurned
                    )

                if (response.isSuccessful) {
                    val data = response.body()
                    _dailyGoalCheck.value = data
                    Log.d("message", data.toString())
                } else {
                    // 실패한 경우에 대한 처리
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    // 에러 메시지 처리
                    Log.e("error1", errorMessage)
                }
            } catch (e: HttpException) {
                // HTTP 예외 처리 (예: 404, 500 등)
                val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
                // 에러 메시지 처리
                Log.e("error2", errorMessage)
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                val errorMessage = "Error: ${e.message}"
                // 에러 메시지 처리
                Log.e("error3", errorMessage)
            }
        }
    }

    fun checkStatus() {
        viewModelScope.launch {
            try {
                val response =
                    communityApiService.checkStatus()
                _communityStatus.value = "loading"
                if (response.isSuccessful) {
                    val data = response.body()
                    _communityStatus.value = data
                    Log.d("message", data.toString())
                } else {
                    // 실패한 경우에 대한 처리
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    // 에러 메시지 처리
                    Log.e("error1", errorMessage)
                }
            } catch (e: HttpException) {
                // HTTP 예외 처리 (예: 404, 500 등)
                val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
                // 에러 메시지 처리
                Log.e("error2", errorMessage)
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                val errorMessage = "Error: ${e.message}"
                // 에러 메시지 처리
                Log.e("error3", errorMessage)
            }
        }
    }

    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = communityApiService.getCommunityList(0, 20)
                if (response.isSuccessful) {
                    val data = response.body()
                    _allCommunityData.value = data!!
                    Log.d("message", data.toString())
                } else {
                    // 실패한 경우에 대한 처리
                    val errorMessage =
                        "Error: ${response.code()} ${response.message()}"
                    // 에러 메시지 처리
                    Log.e("error1", errorMessage)
                }
            } catch (e: HttpException) {
                // HTTP 예외 처리 (예: 404, 500 등)
                val errorMessage = "HTTP Error: ${e.code()} ${e.message()}"
                // 에러 메시지 처리
                Log.e("error2", errorMessage)
            } catch (e: Exception) {
                // 네트워크 오류 등 예외 처리
                val errorMessage = "Error: ${e.message}"
                // 에러 메시지 처리
                Log.e("error3", errorMessage)
            }
        }
    }

    suspend fun joinCommunity(communityId: Long) {
        // 사용자 인증 토큰이라고 가정하고 토큰을 넣어줌
        val authorizationToken =
            MainApplication.prefs.getValue("accessToken")
        try {
            val response = communityApiService.joinCommunity(authorizationToken, communityId)
            if (response.isSuccessful) {
                if (response.body().equals("success")) {
                    _navigateToDetailScreen.value = true
                }
            } else {
                // 에러 메시지 처리
                Log.e("error3", "errorMessage")
            }
        } catch (e: Exception) {
            val errorMessage = "Error: ${e.message}"
            // 에러 메시지 처리
            Log.e("error3", errorMessage)
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun GroupPage(navController: NavHostController) {
    val mode = remember { mutableStateOf("") }
    val viewModel: CommunityAllViewModel = viewModel()

    val checkStatus = viewModel.communityStatus.collectAsState().value
    LaunchedEffect(viewModel) {
        viewModel.checkStatus()
    }


// StateFlow를 collectAsState를 사용해서 관찰합니다.
    val navigateToDetailScreen = viewModel.navigateToDetailScreen.collectAsState().value

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenWidth = constraints.maxWidth.toFloat()
            if (!checkStatus.equals("loading")) {
                if (checkStatus.equals("true")) {
                    mode.value = "detail"
                } else if (checkStatus.equals("false")) {
                    mode.value = "search"
                }
            }

            if (mode.value == "search" && navigateToDetailScreen == false) {
                SearchScreen()
            }

            if (mode.value == "detail" || navigateToDetailScreen == true) {
                DetailScreen("MODE") { navController.navigate("chat") }
            }
        }
    }
}

@Composable
fun SearchScreen() {
    val infoModifier = Modifier
        .height(108.dp)
        .fillMaxWidth()
        .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        .defaultShadow()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F1FE))

    val viewModel: CommunityAllViewModel = viewModel()
    val allCommunityData by viewModel.allCommunityData.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData()
    }

    // 여기서 selectedCommunityId와 test를 정의합니다.
    var selectedCommunityId by remember { mutableStateOf<Long?>(null) }
    var test by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    if (test) {
        selectedCommunityId?.let { communityId ->
            BottomSheet(
                closeSheet = { test = false },
                content = {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("해당 커뮤니티에 가입하시겠습니까?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        viewModel.joinCommunity(communityId)
                                        test = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("예")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    test = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("아니오")
                            }
                        }
                    }
                }
            )
        }
    }

    Column {
        MainBar(text = "그룹", infoImg = true)
        allCommunityData?.let { data ->
            data.forEach { communityData ->
                Box(
                    modifier = infoModifier.clickable {
                        test = true
                        selectedCommunityId = communityData.communityId
                    }
                ) {
                    Column {
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = communityData.title,
                            modifier = Modifier.padding(start = 12.dp, top = 12.dp),
                            color = Color(0xFF171A1F),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        InfoRow(
                            communityData.dailyGoalCalories,
                            communityData.dailyGoalSteps,
                            communityData.weeklyMinGoal,
                            communityData.cntUsers
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailScreen(name: String, function: () -> Unit) {
    val scrollState = rememberScrollState()
    val viewModel: CommunityViewModel = viewModel()
    val communityData by viewModel.userCommunityData.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.fetchData(MainApplication.prefs.getValue("accessToken"))
    }
    Column {
        MainBar(text = name, infoImg = true)
        Column(modifier = Modifier
            .verticalScroll(scrollState)) {
            Column() {
                communityData?.let { data ->
                    InfoSection(
                        data.dailyGoalCalories,
                        data.dailyGoalSteps,
                        data.weeklyMinGoal,
                        data.cntUsers
                    )
                    ContentRows(data.weeklyRemainGoal, data.steps, data.caloriesBurned, data.isToday,function)
                    Column(Modifier.padding(start = 20.dp, end = 20.dp)) {
                        val count: Int = data.communityDietList.size
                        val pagerState = rememberPagerState(
                            pageCount = { count }  // 총 페이지 수
                        )
                        DietBox(diet = data.communityDietList, pagerState = pagerState, b = false)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}

@Composable
private fun InfoSection(
    dailyGoalCalories: Int,
    dailyGoalSteps: Int,
    weeklyMinGoal: Int,
    cntUsers: Int
) {
    val infoModifier = Modifier
        .height(66.dp)
        .fillMaxWidth()
        .padding(top = 8.dp, start = 20.dp, end = 20.dp)
        .defaultShadow()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFF5F1FE))

    Box(modifier = infoModifier) {
        InfoRow(dailyGoalCalories, dailyGoalSteps, weeklyMinGoal, cntUsers)
    }
}

@Composable
private fun ContentRows(
    weeklyRemainGoal: Int,
    steps: Int,
    caloriesBurned: Int,
    isToday: Boolean,
    function: () -> Unit
) {
    Row(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 16.dp)) {
        ContentBox(Modifier.weight(1f)) {
            FirstContent(
                weeklyRemainGoal,
                steps,
                caloriesBurned,
                isToday
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        ContentBox(Modifier.weight(1f)) { SecondContent(function) }
    }
}

@Composable
private fun FirstContent(
    weeklyRemainGoal: Int,
    steps: Int,
    caloriesBurned: Int,
    isToday: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    val healthConnectClientState = remember { mutableStateOf<HealthConnectClient?>(null) }
    val healthEventHandlerState = remember { mutableStateOf<HealthEventHandler?>(null) }
    val viewModel: CommunityAllViewModel = viewModel()
    val dailyGoalCheck = viewModel.dailyGoalCheck.collectAsState().value

    // steps와 caloriesBurned를 상태로 저장
    val (currentSteps, setCurrentSteps) = remember { mutableIntStateOf(steps) }
    val (currentCaloriesBurned, setCurrentCaloriesBurned) = remember {
        mutableIntStateOf(caloriesBurned)
    }

    val buttonTextState = remember { mutableStateOf("일일 목표 인증") }
    val buttonEnabledState = remember { mutableStateOf(!isToday) }
    val weeklyRemainGoalState = remember { mutableStateOf(weeklyRemainGoal) }

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
    LaunchedEffect(dailyGoalCheck) {
        if (dailyGoalCheck == "success") {
            if (weeklyRemainGoalState.value > 0) {
                weeklyRemainGoalState.value -= 1
//                if (weeklyRemainGoalState.value == 0) {
//                    buttonTextState.value = "목표 인증 완료"
//                    buttonEnabledState.value = false
//                }
                buttonTextState.value = "목표 인증 완료"
                buttonEnabledState.value = false
            }
            // 토스트 메시지 표시
            Toast.makeText(context, "목표 인증 완료", Toast.LENGTH_SHORT).show()
        } else if (dailyGoalCheck == "fail") {
            // 토스트 메시지 표시
            Toast.makeText(context, "목표 인증 실패", Toast.LENGTH_SHORT).show()
        }

    }

    Column {
        Text("일일 목표", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        val textToDisplay = if (weeklyRemainGoalState.value == 0) {
            "목표 인증 완료"
        } else {
            "이번 주는 ${weeklyRemainGoalState.value}회 남았어요."
        }
        Text(
            text = textToDisplay,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF323743)
        )

        Spacer(modifier = Modifier.weight(1f))

        Row {
            Column {
                Text("걸음 수", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text(
                    "$currentSteps 걸음",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF323743)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column {
                Text("소모 칼로리", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
                Text(
                    "$currentCaloriesBurned kcal",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF323743)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                healthEventHandlerState.value?.readExerciseData()
                    ?.let { (newSteps, newCaloriesBurned) ->
                        if (newSteps != currentSteps || newCaloriesBurned != currentCaloriesBurned) {
                            setCurrentSteps(newSteps)
                            setCurrentCaloriesBurned(newCaloriesBurned)
                        }
                        viewModel.dailyGoalCheck(newSteps, newCaloriesBurned)
                    }
            },
            enabled = buttonEnabledState.value,
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
            Text(text = buttonTextState.value, fontFamily = Pretend, color = Color.White)
        }
    }
}


@Composable
fun SecondContent(function: () -> Unit) {
    Column(modifier = Modifier.clickable { function.invoke() }) {
        Text("그룹 채팅", fontSize = 12.sp, color = Color(0xFF9095A1), lineHeight = 20.sp)
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 0.dp,
                        bottomStart = 8.dp
                    )
                )
                .background(Color(0xFFF3F4F6))
                .padding(top = 13.dp, start = 10.dp, end = 10.dp)
        ) {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "채팅방 이동",
                    color = Color(0xFF323743),
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 26.sp
                )
                Spacer(modifier = Modifier.weight(1f))
            }

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

fun Modifier.defaultShadow() = this.shadow(
    elevation = 2.dp,
    shape = RoundedCornerShape(8.dp),
    ambientColor = Color(0xFF171A1F),
    spotColor = Color(0xFF171A1F)
)


