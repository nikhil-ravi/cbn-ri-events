package com.example.calendar.domain.events

// I created this to add a sticky header for each date, but the lazy column doesn't support sticky headers
// inside the "items" scope, so I had to use a different approach
sealed class EventOrSeparator {
    data class EventItem(val event: Event) : EventOrSeparator()
    data class SeparatorItem(val date: String) : EventOrSeparator()
}
