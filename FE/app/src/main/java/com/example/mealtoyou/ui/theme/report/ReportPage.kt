package com.example.mealtoyou.ui.theme.report

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.ui.theme.shared.MainBar
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.common.shader.DynamicShader

private val bodyModel =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(3, 3.3, 3.2, 3.1, 3.5, 3.4)
            series(2, 2.3, 2.2, 2.1, 2.5, 2.4)
            series(1.2, 1.1, 1.1, 1.4, 1.2, 1.4)
        },
    )

private val activityModel =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(3, 3.3, 3.2, 3.1, 3.5, 3.4)
            series(2, 2.3, 2.2, 2.1, 2.5, 2.4)
        },
    )

private val sleepModel =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(3, 3.3, 3.2, 3.1, 3.5, 3.4)
        },
    )

@Composable
fun ReportPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainBar(text = "분석", infoImg = true)
            ContentBody(Modifier.weight(1f))
        }
    }
}

@Composable
fun ContentBody(modifier: Modifier) {
    val scrollState = rememberScrollState()
    var dateMenuPointer by remember { mutableIntStateOf(0) }
    var titleMenuPointer by remember { mutableIntStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            TitleMenu(titleMenuPointer) { newPointer ->
                titleMenuPointer = newPointer
                dateMenuPointer = 0
            }
            DateMenu(dateMenuPointer) { newPointer ->
                dateMenuPointer = newPointer
            }
            Spacer(modifier = Modifier.height(16.dp))
            DataDisplayBox(titleMenuPointer)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun DataDisplayBox(titleMenuPointer: Int) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(8.dp),
                ambientColor = Color(0xFF171A1F),
                spotColor = Color(0xFF171A1F)
            )
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {

        Crossfade(targetState = titleMenuPointer, label = "깔끔한 이동") { pointer ->
            Column {
                when (pointer) {
                    0 -> {
                        LineChart(listOf("체중", "골격근량", "체지방량"), bodyModel)
                        Spacer(modifier = Modifier.height(15.dp))
                        BodyInfoRow()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tempor mollit labore eiusmod excepteur mollit adipisicing ullamco adipisicing dolore sunt ut minim dolor qui ipsum esse laboris. Sit voluptate consectetur p",
                            color = Color(0xFF9095A1),
                            fontSize = 12.sp,
                            lineHeight = 20.sp
                        )
                    }

                    1 -> {
                        LineChart(listOf("걸음수", "소모 칼로리"), activityModel)
                        Spacer(modifier = Modifier.height(15.dp))
                        ActivityInfoRow()
                    }

                    2 -> {
                        LineChart(listOf("수면 시간"), sleepModel)
                        Spacer(modifier = Modifier.height(15.dp))
                        SleepInfoRow()
                    }
                }
            }
        }

    }
}

@Composable
fun BodyInfoRow() {
    Row {
        InfoColumn("기초 대사량", "1,829 kcal")
        Spacer(modifier = Modifier.width(17.dp))
        InfoColumn("bmi 지수", "18 (정상)")
        Spacer(modifier = Modifier.width(17.dp))
        InfoColumn("CID 유형", "표준체중 일반형(I자)")
    }
}

@Composable
fun ActivityInfoRow() {
    Row {
        InfoColumn("걸음수 평균", "9,283 걸음")
        Spacer(modifier = Modifier.width(17.dp))
        InfoColumn("소모 칼로리 평균", "482 kcal")
    }
}

@Composable
fun SleepInfoRow() {
    Row {
        InfoColumn("수면 평균", "8시간")
        Spacer(modifier = Modifier.width(17.dp))
        InfoColumn("수면 점수", "86점")
    }
}

@Composable
fun InfoColumn(title: String, detail: String) {
    Column {
        Text(text = title, color = Color(0xFF9095A1), fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = detail,
            color = Color(0xFF323743),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LineChart(legendTexts: List<String>, model: CartesianChartModel) {
    val chartColors = listOf(
        Color(0xFF6D31ED), // Purple
        Color(0xFFFF56A5), // Pink
        Color(0xFF15ABFF)  // Blue
    )

    Surface(color = Color.White) {
        Column {
            LegendCentered(chartColors, legendTexts)
            Spacer(modifier = Modifier.height(10.dp))
            ChartContent(chartColors, model)
        }
    }
}

@Composable
fun ChartContent(chartColors: List<Color>, model: CartesianChartModel) {
    val lineSpecs = chartColors.map { color ->
        rememberLineSpec(
            shader = DynamicShader.color(color),
            thickness = 2.5f.dp
        )
    }

    CartesianChartHost(
        modifier = Modifier
            .fillMaxWidth()
            .height(342.dp)
            .padding(0.dp),
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                lineSpecs,
                axisValueOverrider = AxisValueOverrider.fixed(maxY = 4f),
            ),
            startAxis = rememberStartAxisWithCustomPlacer()
        ),
        model = model
    )
}

@Composable
fun rememberStartAxisWithCustomPlacer(): VerticalAxis<AxisPosition.Vertical.Start> {
    return rememberStartAxis(
        label = null,
        itemPlacer = remember {
            AxisItemPlacer.Vertical.count(count = { 10 }) // 최대 10개 눈금 표시
        },
        tickLength = 0.dp
    )
}

@Composable
fun LegendCentered(chartColors: List<Color>, legends: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth() // 가로 방향으로 최대 공간 채우기
            .padding(8.dp), // 원하는 패딩 설정
        horizontalArrangement = Arrangement.Center // 가로 방향으로 중앙 정렬
    ) {
        // 범례 요소 생성
        legends.forEachIndexed { index, legend ->
            LegendItem(chartColors[index], legend, 8.dp) // 각 범례 아이템
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String, iconPadding: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically, // 아이콘과 텍스트 수직 중앙 정렬
        modifier = Modifier.padding(horizontal = iconPadding) // 아이콘 간 패딩 조정
    ) {
        Box(
            modifier = Modifier
                .size(16.dp) // 아이콘 크기
                .background(color, shape = CircleShape) // 아이콘 모양 및 색상
        )
        Spacer(Modifier.width(8.dp)) // 아이콘과 텍스트 사이 공간
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}
