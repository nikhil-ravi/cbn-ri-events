package com.example.calendar.data.remote

data class EventDto (
    val id: String,
    val summary: String? = null,
    val location: String? = null,
    val start: StartTimeDto = StartTimeDto(),
    val end: StartTimeDto = StartTimeDto(),
    val description: String? = null,
    val status: String? = null,
)

data class StartTimeDto (
    val dateTime: String? = null,
    val timeZone: String? = null,
    val date: String? = null,
)

data class EventListDto (
    val items: List<EventDto> = listOf(),
    val nextPageToken: String? = null,
    val nextSyncToken: String? = null,
)