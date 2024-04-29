package com.example.mealtoyou.ui.theme.shared

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.Pretend

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Home", "mainPage", R.drawable.home),
        NavigationItem("분석", "분석", R.drawable.ananlys),
        NavigationItem("식단", "식단", R.drawable.diet),
        NavigationItem("그룹", "그룹", R.drawable.group),
        NavigationItem("마이", "마이", R.drawable.my)
    )
    val selectedColor = Color(0xFF6D31ED)
    val unselectedColor = Color(0xFF565D6D)

    Box {
        Surface(
            modifier = Modifier
                .matchParentSize()
                .offset(y = (-0.2f).dp),
            color = Color(0xFF171A1F).copy(alpha = 0.25f),
            shadowElevation = 3.dp,
        ) {}

        // NavigationBar 추가
        NavigationBar(
            modifier = Modifier
                .background(color = Color.White)
                .height(80.dp),
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            items.forEach { (label, route, iconRes) ->
                val isSelected = navController.currentDestination?.route == route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            // navController.navigate(route)
                        }
                    },
                    icon = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height((8).dp))
                            Icon(painterResource(id = iconRes), contentDescription = label)
                            // Icon과 Text 사이의 간격을 조정하려면 여기에 Spacer를 추가하세요.
                            Spacer(Modifier.height((0).dp)) // 간격 조정
                            Text(label, fontFamily = Pretend, fontSize = 12.sp)
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = selectedColor,
                        selectedTextColor = selectedColor,
                        unselectedIconColor = unselectedColor,
                        unselectedTextColor = unselectedColor,
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }
}


// NavigationItem 데이터 클래스를 정의합니다.
data class NavigationItem(val label: String, val route: String, val icon: Int)