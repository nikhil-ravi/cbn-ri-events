package com.example.calendar.domain.events

data class Event(
    val id: String,
    val summary: String? = null,
    val location: String? = null,
    val date: String? = null,
    val startTime: String? = null,
    val duration: String? = null,
    val description: EventMetadata = EventMetadata(),
    val status: String? = null,
    val recurringEventId: String? = null,
)
