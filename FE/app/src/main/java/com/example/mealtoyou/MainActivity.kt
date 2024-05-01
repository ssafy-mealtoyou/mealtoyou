package com.example.mealtoyou

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mealtoyou.ui.theme.login.LoginPage
import com.example.mealtoyou.ui.theme.main.MainPage
import com.example.mealtoyou.ui.theme.shared.BottomNavigationBar

class MainActivity : ComponentActivity() {

    @Composable
    fun SetupSystemBars() {
        SideEffect {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            window.statusBarColor = Color(0xFFFFFFFF).toArgb()
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealToYouTheme {
                val navController = rememberNavController()
                SetupSystemBars()
                MainScreen(navController)
            }
        }
    }

    @Composable
    fun MainScreen(navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val showBottomBar = currentRoute != "login"
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
        ) { innerPadding ->
            Surface(modifier = Modifier.padding(innerPadding)) {
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginPage(navController)
                    }
                    composable("mainPage") {
                        MainPage()
                    }
                }
            }
        }
    }
}