package com.example.calendar.presentation.home.notices

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.calendar.domain.events.Event

@Composable
fun NoticeItem(
    notice: Event,
    modifier: Modifier = Modifier,
) {
    val dateText = when (notice.date) {
        "Today" -> "today"
        "Tomorrow" -> "tomorrow"
        else -> "on ${notice.date}"
    }
    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {

        Text(text = "${notice.summary} originally scheduled to start at ${notice.startTime} $dateText has been cancelled.")
    }
}