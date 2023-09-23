package com.example.calendar.data.mappers

import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.remote.EventDto
import com.example.calendar.domain.events.Event
import com.example.calendar.domain.events.EventMetadata
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun EventDto.toEventEntity(): EventEntity {
    val startDateTime = start.dateTime ?: ZonedDateTime
        .of(
            LocalDate.parse(start.date).atStartOfDay(),
            ZonedDateTime.now().zone
        ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx"))
    val endDateTime = end.dateTime ?: ZonedDateTime.of(
        LocalDate.parse(end.date).atStartOfDay(),
        ZonedDateTime.now().zone
    ).format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssxxx")
    )
    return EventEntity(
        id = id,
        summary = summary,
        location = location,
        startDateTime = startDateTime,
        startTimeZone = start.timeZone,
        endDateTime = endDateTime,
        endTimeZone = end.timeZone,
        description = description,
        status = status,
    )
}

fun EventEntity.toEvent(): Event {
    val parsedStartDateTime = ZonedDateTime.parse(startDateTime).toLocalDateTime()
    val parsedEndDateTime = ZonedDateTime.parse(endDateTime).toLocalDateTime()

    var duration = calculateDuration(parsedStartDateTime, parsedEndDateTime)
    val startTimeText = if (duration == "1 d") ""
    else parsedStartDateTime.format(DateTimeFormatter.ofPattern("h:mm a"))
    duration = if (duration == "1 d") "All Day"
    else duration

    val metadata = if (description != null) try {
        Json.decodeFromString<EventMetadata>(
            description.replace(
                Regex("<[^>]*>"),
                ""
            )
        )
    } catch (e: Exception) {
        EventMetadata()
    }
    else EventMetadata()

    return Event(
        id = id,
        summary = summary,
        location = location,
        date = dateToString(parsedStartDateTime),
        startTime = startTimeText,
        duration = duration,
        description = metadata,
        status = status,
    )
}


fun dateToString(date: LocalDateTime?): String {
    if (date == null) return ""
    val today = LocalDate.now()

    return when (date.toLocalDate()) {
        today -> "Today"
        today.plusDays(1) -> "Tomorrow"
        else -> date.format(DateTimeFormatter.ofPattern("EEE, MMM d"))
    }
}


fun calculateDuration(startTimeDT: LocalDateTime?, endTimeDT: LocalDateTime?): String {
    if (startTimeDT == null || endTimeDT == null) return "0 min"

    val duration = Duration.between(startTimeDT, endTimeDT)
    val days = duration.toDays()
    val hours = duration.toHours() % 24
    val minutes = duration.toMinutes() % 60

    val durationString = buildString {
        if (days > 0) append("$days d ")
        if (hours > 0) append("$hours h ")
        if (minutes > 0 || (days == 0L && hours == 0L)) append("$minutes min")
    }

    return durationString.trim()

}