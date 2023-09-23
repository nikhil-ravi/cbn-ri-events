package com.example.calendar.presentation.events

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.domain.events.Event
import com.example.calendar.domain.events.EventMetadata
import com.example.calendar.ui.theme.CalendarTheme


@Composable
fun EventItem(event: Event, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clip(CardDefaults.shape)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor =
            if (event.description.special) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                if (event.startTime!!.isNotEmpty()) {
                    Text(
                        text = event.startTime, //TODO: formattedTime,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    )
                    Separator()
                }
                Text(
                    text = event.duration!!, //TODO: duration,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                )
                if (event.description.instructor != null) {
                    Separator()
                    Text(
                        text = event.description.instructor,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.summary ?: "Event Name",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = if (event.status != "confirmed") MaterialTheme.colorScheme.onSecondaryContainer
                        else Color.Black,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = event.location ?: "Location",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}


@Composable
fun Separator(separatorText: String = "|", separatorSize: Int = 16) {
    Spacer(modifier = Modifier.padding(2.dp))
    Text(
        text = separatorText,
        style = TextStyle(
            fontSize = separatorSize.sp,
            color = Color.Gray
        )
    )
    Spacer(modifier = Modifier.padding(2.dp))
}


@Preview
@Composable
fun AllDayEventItemPreview() {
    CalendarTheme(useDarkTheme = true) {
        EventItem(
            event = Event(
                id = "1",
                summary = "A very long event name that should be truncated",
                location = "Test Location",
                description = EventMetadata(
                    special = true
                ),
                startTime = "",
                date = "2023-07-01",
                duration = "All day",
                status = "confirmed",
            )
        )
    }
}

@Preview
@Composable
fun EventItemPreview() {
    CalendarTheme {
        EventItem(
            event = Event(
                id = "1",
                summary = "A very long event name that should be truncated",
                location = "Test Location",
                description = EventMetadata(
                    instructor = "John long name Doe that should be truncated",
                    special = false
                ),
                startTime = "8:30 am",
                date = "2023-07-01",
                duration = "1h 20 min",
                status = "cancelled",
            )
        )
    }
}
