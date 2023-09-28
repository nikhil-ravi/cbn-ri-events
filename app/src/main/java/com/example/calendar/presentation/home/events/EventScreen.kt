package com.example.calendar.presentation.home.events

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.calendar.domain.events.DateEvents
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventScreen(
    events: LazyPagingItems<DateEvents>,
    favoriteEvents: List<String>,
    onEventFavorite: (String) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
//    LaunchedEffect(key1 = events.loadState) {
//        if (events.loadState.refresh is LoadState.Error) {
//            Toast.makeText(
//                context,
//                "Error loading events: " + (events.loadState.refresh as LoadState.Error).error.message,
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf<LocalDate?>(currentDate) }

    val state = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstVisibleWeekDate = currentDate,
    )

    var idx by remember { mutableStateOf<Map<LocalDate, Int>>(emptyMap()) }

    LaunchedEffect(key1 = events.loadState) {
        Log.d("EventScreen", "Refreshing idx")
        idx = emptyMap()
        for (index in 0 until events.itemCount) {
            val event = events.peek(index)
            val date = LocalDate.parse(event?.date)
            idx = idx + mapOf(date!! to index)
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = events.loadState.refresh == LoadState.Loading,
        onRefresh = { events.refresh() },
        refreshThreshold = 150.dp,
    )
    Scaffold(
        topBar = {
            WeekCalendar(
                modifier = Modifier.padding(vertical = 4.dp),
                state = state,
                calendarScrollPaged = false,
                dayContent = { day ->
                    Day(day.date, selected = selection == day.date) {
                        selection = it
                    }
                },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = selection != currentDate) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            state.animateScrollToWeek(currentDate)
                        }
                        selection = currentDate
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                ) {
                    Text(
                        text = "Today",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .pullRefresh(pullRefreshState)
        ) {
            HorizontalDivider()
            if (events.loadState.refresh == LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            DateItem(
                dateEvents =
                if (idx[selection] != null)
                    events[idx[selection]!!]
                else null,
                onEventFavorite = onEventFavorite,
                favoriteEvents = favoriteEvents,
            )
            if (events.loadState.append == LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
            PullRefreshIndicator(
                refreshing = events.loadState.refresh == LoadState.Loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
            )
        }
    }
}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    date: LocalDate,
    selected: Boolean = false,
    onClick: (LocalDate) -> Unit = {},
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Surface(
        modifier = Modifier
            // If paged scrolling is disabled (calendarScrollPaged = false),
            // you must set the day width on the WeekCalendar!
            .width(screenWidth / 7)
            .padding(2.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(
                shape = RoundedCornerShape(8.dp),
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else Color.Transparent,
            )
            .wrapContentHeight()
            .clickable { onClick(date) },
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.month.displayText(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = date.dayOfWeek.displayText(),
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}