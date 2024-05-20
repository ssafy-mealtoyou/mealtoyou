import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mealtoyou.MainApplication
import com.example.mealtoyou.R
import com.example.mealtoyou.ui.theme.diet.DietViewModel
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.WeekCalendarState
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.flow.filter
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(viewModel: DietViewModel) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(5.dp),
    ) {
        val state = rememberWeekCalendarState(
            startDate = startDate,
            endDate = endDate,
            firstVisibleWeekDate = currentDate,
        )
        val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)

        WeekCalendar(
            modifier = Modifier.background(Color.White),
            state = state,
            dayContent = { day ->
                Day(day.date, isSelected = selection == day.date) { clicked ->
                    if (selection != clicked) {
                        selection = clicked
                        viewModel.fetchData(MainApplication.prefs.getValue("accessToken"), clicked.toString())
                    }
                }
            },
        )
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
fun Day(date: LocalDate, isSelected: Boolean, onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
            .height(60.dp)
                                  .background(Color(0xF8F9FA                                                       ).copy(alpha = 0.9f),shape = RoundedCornerShape(5.dp))
            .wrapContentHeight()
            .clickable { onClick(date) },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {

            Text(
                text = dateFormatter.format(date),
                fontSize = 15.sp,
                color = if (isSelected) Color.Black else Color.DarkGray,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                fontSize = 12.sp,
                color = Color.Black,
                fontWeight = FontWeight.Light,
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color(0xFF6D31ED).copy(alpha = 0.2f),shape = RoundedCornerShape(5.dp))
                    .border(1.dp, Color(0xFF6D31ED), shape = RoundedCornerShape(5.dp))
                    .align(Alignment.BottomCenter),
            )
        }
    }
}

fun getWeekPageTitle(week: Week): String {
    val firstDate = week.days.first().date
    val lastDate = week.days.last().date
    return when {
        firstDate.yearMonth == lastDate.yearMonth -> {
            firstDate.yearMonth.displayText()
        }

        firstDate.year == lastDate.year -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.month.displayText(short = true)}"
        }

        else -> {
            "${firstDate.yearMonth.displayText()} - ${lastDate.yearMonth.displayText()}"
        }
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.year}년  ${this.month.displayText(short = short)}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.KOREAN)
}

@Composable
fun rememberFirstVisibleWeekAfterScroll(state: WeekCalendarState): Week {
    val visibleWeek = remember(state) { mutableStateOf(state.firstVisibleWeek) }
    LaunchedEffect(state) {
        snapshotFlow { state.isScrollInProgress }
            .filter { scrolling -> !scrolling }
            .collect { visibleWeek.value = state.firstVisibleWeek }
    }
    return visibleWeek.value
}