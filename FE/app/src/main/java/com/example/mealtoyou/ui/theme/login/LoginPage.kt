package com.example.mealtoyou.ui.theme.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.mealtoyou.data.model.request.LoginReqDto
import com.example.mealtoyou.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginPage(navController: NavHostController, googleSignInClient: GoogleSignInClient) {
    val context = LocalContext.current
    val signInIntent = googleSignInClient.signInIntent
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            Log.d("result", result.data.toString())
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                // 구글 ID 토큰
                val idToken = account.idToken
                Log.d("Login", idToken.toString())
                // TODO: 서버로 ID 토큰을 보내고 처리
                idToken?.let { LoginReqDto(it) }?.let { dto ->
                    // 비동기 로그인 시도
                    CoroutineScope(Dispatchers.Main).launch {
                        AuthRepository().login(dto).collect { loginSuccess ->
                            if (loginSuccess) {
                                // 로그인 성공 처리
                                Log.d("Login", "로그인 성공")
                                navController.navigate("mainPage") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                // 로그인 실패 처리
                                Log.d("Login", "로그인 실패1")
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                // 로그인 실패 처리
                Log.d("Login", "로그인 실패2")
                Log.d("Login", e.stackTraceToString())
            }
        }
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF6D31ED)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainImage()
            MainIcon()
            GoogleButton(
                onClick = {
                    Log.d("google login", "구글 로그인 버튼 클릭")
                    launcher.launch(signInIntent)
                }
            )
        }
    }
}