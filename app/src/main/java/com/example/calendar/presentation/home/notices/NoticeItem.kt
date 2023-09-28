package com.example.calendar.presentation.home.notices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
            .safeContentPadding(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    ) {
        if (notice.description.cancelled) {
            Text(
                text = "${notice.summary} originally scheduled to start at " +
                        "${notice.startTime} $dateText has been cancelled.",
                modifier = Modifier
                    .padding(16.dp),

                )
        } else if (notice.description.is_notice) {
            Text(
                text = "${notice.summary}: ${
                    notice.description.description!!.replace(
                        "&lt;date&gt;",
                        dateText
                    )
                }",
                modifier = Modifier
                    .padding(16.dp),

                )
        }
    }
}