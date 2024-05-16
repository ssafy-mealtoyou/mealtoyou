package com.example.mealtoyou

import android.content.pm.PackageManager
import android.os.Build
import ExerciseDataWorker
import SupplementViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mealtoyou.ui.theme.MealToYouTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.health.connect.client.HealthConnectClient
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.mealtoyou.handler.HealthEventHandler
import com.example.mealtoyou.ui.theme.diet.DietPage
import com.example.mealtoyou.ui.theme.group.ChatScreen
import com.example.mealtoyou.ui.theme.group.GroupPage
import com.example.mealtoyou.ui.theme.mypage.MyPage
import com.example.mealtoyou.ui.theme.login.LoginPage
import com.example.mealtoyou.ui.theme.main.MainPage
import com.example.mealtoyou.ui.theme.report.ReportPage
import com.example.mealtoyou.ui.theme.shared.BottomNavigationBar
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import android.util.Log

import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealtoyou.handler.FcmEventHandler
import com.example.mealtoyou.viewmodel.HealthViewModel
import com.example.mealtoyou.data.repository.PreferenceUtil
import com.example.mealtoyou.ui.theme.diet.DietViewModel
import com.example.mealtoyou.ui.theme.group.SearchScreen
import com.example.mealtoyou.viewmodel.AIFeedbackViewModel
import com.example.mealtoyou.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.messaging.FirebaseMessaging
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private lateinit var healthConnectClient: HealthConnectClient
    private lateinit var healthEventHandler: HealthEventHandler
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var userViewModel: UserViewModel
    private lateinit var aiFeedbackViewModel: AIFeedbackViewModel

    @Composable
    fun SetupSystemBars() {
        SideEffect {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.statusBarColor = Color(0xFFFFFFFF).toArgb()
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = true
        }
    }

    private fun isHealthConnectInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            packageManager.getPackageInfo("com.google.android.apps.healthdata", 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isInstalled = isHealthConnectInstalled(this)
        var showDialog by mutableStateOf(false)
        var errorMessage by mutableStateOf("")

        if (!isInstalled) {
            errorMessage =
                "데이터 자동 연동을 원하시면 헬스 커넥트 설치 및 권한을 허용해주시길 바랍니다"
//                "Health Connect application is required to use this app. Please install it from the Play Store."
            showDialog = true
        } else {
            healthConnectClient = HealthConnectClient.getOrCreate(this)
            healthEventHandler = HealthEventHandler(this, healthConnectClient)
            setupPeriodicWork()
        }
        val supplementViewModel: SupplementViewModel by viewModels()
        val healthViewModel: HealthViewModel by viewModels()

        this.userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        this.aiFeedbackViewModel = ViewModelProvider(this)[AIFeedbackViewModel::class.java]

        // 액티비티가 생성될 때 데이터 로드
        supplementViewModel.supplementScreen()
        // GoogleSignInOptions 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("390865306655-enuqjnl61ofnm3c5anf7jiua1mcmjtpk.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContent {

            if (showDialog) {
                ShowErrorDialog(errorMessage) {
                    //finish() // 액티비티 종료
                    showDialog = false
                }
            }

            MealToYouTheme {
                val navController = rememberNavController()
                SetupSystemBars()

                MainScreen(navController, supplementViewModel, healthViewModel)
            }

        }
        if(MainApplication.prefs.getValue("accessToken").isNotEmpty()){
            sendFcmToken()
        }
        setupPeriodicWork()
    }

    @Composable
    fun ShowErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Health Connect Required", fontSize = 18.sp) },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        )
    }

    private fun sendFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "FCM Token: $token")

            FcmEventHandler().sendFcmToken(token)
        }
    }


    private fun setupPeriodicWork() {
        val currentTime = LocalTime.now()
        val targetTime = LocalTime.of(
            if (currentTime.minute >= 50) (currentTime.hour + 1)%24 else currentTime.hour,
            50
        )
        val delay = Duration.between(currentTime, targetTime).toMinutes().coerceAtLeast(0L)

        val exerciseDataWorkRequest =
            PeriodicWorkRequestBuilder<ExerciseDataWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "exerciseDataWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            exerciseDataWorkRequest
        )
    }

    @Composable
    fun MainScreen(navController: NavHostController, supplementViewModel: SupplementViewModel, healthViewModel: HealthViewModel) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val showBottomBar = currentRoute != "login" && currentRoute != "chat"
        // TODO: 여기에 accessToken검증과정 추가예정
        if(MainApplication.prefs.getValue("accessToken").isNotEmpty()){

        }
        val startDestination = if (MainApplication.prefs.getValue("accessToken").isNotEmpty()) {
            "mainPage"
        } else {
            "login"
        }
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->

            Surface(modifier = Modifier.padding(innerPadding)) {
                NavHost(
                    navController = navController,
                    startDestination =startDestination,
//                    startDestination = "mainPage",
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(300)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Start,
                            tween(300)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            tween(300)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.End,
                            tween(300)
                        )
                    }
                ) {
                    composable("login") {
                        LoginPage(navController, googleSignInClient)
                    }
                    composable("mainPage") {
                        MainPage(supplementViewModel)
                    }
                    composable("분석") {
                        ReportPage()
                    }
                    composable("식단") {
                        val viewModel: DietViewModel = viewModel() // viewModel 생성
                        DietPage(viewModel)
                    }
                    composable("그룹") {
                        GroupPage(navController)
                    }
                    composable("마이") {
                        MyPage(supplementViewModel, healthViewModel,navController)
                    }
                    composable("chat") {
                        ChatScreen()
                    }
                }
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(POST_NOTIFICATIONS)
            }
        }
    }
}
