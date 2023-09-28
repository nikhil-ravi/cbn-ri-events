package com.example.calendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class EventEntity(
    val id: String,
    val summary: String? = null,
    val location: String? = null,
    val startDateTime: String? = null,
    val startTimeZone: String? = null,
    val endDateTime: String? = null,
    val endTimeZone: String? = null,
    val description: String? = null,
    val status: String? = null,
    val recurringEventId: String? = null,
)


@Serializable
@Entity
data class DateEventsEntity(
    @PrimaryKey
    val date: String,
    @TypeConverters(EntityConverters::class)
    val events: List<EventEntity>
)

class EntityConverters {
    @TypeConverter
    fun toJsonString(events: List<EventEntity>): String {
        return Json.encodeToString(events)
    }

    @TypeConverter
    fun fromJsonString(jsonString: String): List<EventEntity> {
        return Json.decodeFromString(jsonString)
    }
}

