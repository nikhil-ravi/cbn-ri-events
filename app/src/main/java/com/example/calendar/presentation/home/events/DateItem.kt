package com.example.calendar.presentation.home.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.calendar.domain.events.DateEvents

@Composable
fun DateItem(
    modifier: Modifier = Modifier,
    dateEvents: DateEvents?,
    onEventFavorite: (String) -> Unit,
    favoriteEvents: List<String>
) {
    if (dateEvents == null) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No events")
            }
        }
    } else {
        LazyColumn {

            items(dateEvents.events.size) { index ->
                val event = dateEvents.events[index]
                EventItem(
                    event = event,
                    onEventFavorite = onEventFavorite,
                    favorite = favoriteEvents.contains(event.id) ||
                            favoriteEvents.any { event.id.startsWith(it) },
                )
            }
        }
    }
}