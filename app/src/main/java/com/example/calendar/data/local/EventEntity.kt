package com.example.calendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventEntity(
    @PrimaryKey
    val id: String,
    val summary: String? = null,
    val location: String? = null,
    val startDateTime: String? = null,
    val startTimeZone: String? = null,
    val endDateTime: String? = null,
    val endTimeZone: String? = null,
    val description: String? = null,
    val status: String? = null,
)

