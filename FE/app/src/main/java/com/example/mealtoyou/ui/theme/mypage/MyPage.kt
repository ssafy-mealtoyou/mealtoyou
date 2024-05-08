package com.example.mealtoyou.ui.theme.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.HealthConnectClient
import com.example.mealtoyou.R
import com.example.mealtoyou.handler.HealthEventHandler
import com.example.mealtoyou.ui.theme.Pretend
import com.example.mealtoyou.ui.theme.main.stage.Challenge
import com.example.mealtoyou.ui.theme.main.stage.DrugInfo
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.example.mealtoyou.ui.theme.shared.shadowModifier

@Composable
fun MyPage(healthEventHandler: HealthEventHandler, healthConnectClient: HealthConnectClient) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column {


            MainBar(text = "마이페이지", infoImg = false)

            val scrollState = rememberScrollState()
            Column(Modifier.verticalScroll(scrollState)) {
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.main_image),
                        contentDescription = "Sample Food Image",
                        modifier = Modifier
                            .height(211.dp)
                            .fillMaxWidth(0.55f)
                            .clip(CircleShape),

                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                Column(Modifier.padding(20.dp))
                {
                    Box(
                        modifier = shadowModifier()
                            .height(156.dp)
                            .fillMaxWidth()
                            .shadow(
                                elevation = 2.dp,
                                shape = RoundedCornerShape(8.dp),
                                ambientColor = Color(0xFF171A1F),
                                spotColor = Color(0xFF171A1F)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "나의 체성분 정보",
                                color = Color(0xFF323743),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = "골격근량",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9095A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(15.dp))
                                Text(
                                    text = "32kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(20.dp))
                                Text(
                                    text = "체지방량",
                                    fontSize = 12.sp,
                                    color = Color(0xFF9095A1),
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.width(15.dp))
                                Text(
                                    text = "22kg",
                                    color = Color(0xff171A1F),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row {
                                Spacer(modifier = Modifier.weight(1f))
                                Button(
                                    onClick = { },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                    border = BorderStroke(1.dp, color = Color(0xFF6D31ED)),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(101.dp),
                                ) {
                                    Text(
                                        text = "직접 등록",
                                        fontFamily = Pretend,
                                        color = Color(0xFF6D31ED)
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {healthEventHandler.readHealthData()},
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(
                                            0xFF6D31ED
                                        )
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .width(158.dp)
                                        .shadow(
                                            elevation = 10.dp,
                                            shape = RoundedCornerShape(8.dp),
                                            clip = true,
                                            ambientColor = Color(0xFF171A1F).copy(alpha = 0.15f),
                                            spotColor = Color(0xFF171A1F).copy(alpha = 0.15f)
                                        ),
                                ) {
                                    Text(
                                        text = "갤럭시 워치로 등록",
                                        fontFamily = Pretend,
                                        color = Color.White
                                    )
                                }
                            }

                        }
                    }

                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .padding(end = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    DonEatTimer(Modifier.weight(9f), Color(0xFFF1FDE9), true)

                    Spacer(Modifier.weight(1f))
                    EditWeight(Modifier.weight(9f), Color(0xFFFFFAE1), true)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp)
                        .padding(end = 20.dp)
                        .padding(bottom = 20.dp)
                ) {
                    Challenge(Modifier.weight(9f), Color(0xFFF5F1FE), true)

                    Spacer(Modifier.weight(1f))
                    DrugInfo(Modifier.weight(9f), Color(0xFFF0F9FF), true)
                }
            }

        }

    }
}

@Composable
fun DonEatTimer(modifier: Modifier, color: Color, setupAble: Boolean) {
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
                    text = "단식 설정",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    Image(
                        painter = painterResource(id = R.drawable.gear),
                        contentDescription = "gear",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditWeight(modifier: Modifier, color: Color, setupAble: Boolean) {
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
                    text = "체중 등록",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Pretend,
                    lineHeight = 20.sp,
                    color = Color(0xFF9095A1),
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
                if (setupAble) {
                    Image(
                        painter = painterResource(id = R.drawable.gear),
                        contentDescription = "gear",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}