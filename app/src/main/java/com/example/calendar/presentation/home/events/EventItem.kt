package com.example.calendar.presentation.home.events

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendar.domain.events.Event
import com.example.calendar.domain.events.EventMetadata
import com.example.calendar.ui.theme.CalendarTheme


@Composable
fun EventItem(
    modifier: Modifier = Modifier,
    event: Event,
    favorite: Boolean = false,
    onEventFavorite: (String) -> Unit
) {
    var isDialogOpen by remember { mutableStateOf(false) }
    var userChoice by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp)
//            .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                if (event.startTime!!.isNotEmpty()) {
                    Text(
                        text = event.startTime,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    )
                    Separator()
                }
                Text(
                    text = event.duration!!,
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
                        color =
                        if (event.description.special)
                            Color(0xFFFF6292)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration =
                        if (event.description.cancelled)
                            TextDecoration.LineThrough
                        else
                            TextDecoration.None
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
                    text = event.location ?: "Roosevelt Island Senior Center",
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
                    onClick = {
//                        if (event.recurringEventId == null) {
                        onEventFavorite(event.id)
//                        } else {
//                            isDialogOpen = true
//                        }
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint =
                        if (favorite)
                            Color(0xFFFF6292)
                        else
                            MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }

    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                isDialogOpen = false
            },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            title = {
                Text(text = "Favorite Event?")
            },
            text = {
                Text(
                    "Do you want to favorite the entire series or just this event?",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEventFavorite(event.id)
                        isDialogOpen = false
                    }
                ) {
                    Text("Just this")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onEventFavorite(event.recurringEventId!!)
                        isDialogOpen = false
                    }
                ) {
                    Text("Series")
                }
            }
        )

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
fun SpecialEventItemPreviewLight() {
    CalendarTheme(useDarkTheme = false) {
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
            ),
            favorite = true,
            onEventFavorite = {}
        )
    }
}

@Preview
@Composable
fun SpecialEventItemPreviewDark() {
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
            ),
            onEventFavorite = {}
        )
    }
}

@Preview
@Composable
fun NormalEventItemPreviewDark() {
    CalendarTheme(useDarkTheme = true) {
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
            ),
            onEventFavorite = {}
        )
    }
}

@Preview
@Composable
fun NormalEventItemPreviewLight() {
    CalendarTheme(useDarkTheme = false) {
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
            ),
            onEventFavorite = {}
        )
    }
}