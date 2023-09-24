package com.example.calendar.presentation.events

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.calendar.domain.events.Event

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventScreen(
    events: LazyPagingItems<Event>
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = events.loadState) {
        if (events.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error loading events: " + (events.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            if (events.loadState.refresh == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }

            val eventCount = events.itemCount

            var lastEventDate: String? = null

            for (index in 0 until eventCount) {
                val event = events.peek(index)
                val date = event?.date

                if (event != null && lastEventDate != date) {
                    stickyHeader(key = date) {
                        DateHeader(date = event.date!!)
                    }
                }

                if (event != null) {
                    item(key = event.id) {
                        HorizontalDivider()
                        EventItem(
                            event = event,
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                }

                lastEventDate = date
            }

            if (events.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
