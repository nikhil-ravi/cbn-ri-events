package com.example.calendar.domain.events

import kotlinx.serialization.Serializable

@Serializable
data class EventMetadata(
    val description: String? = null,
    val instructor: String? = null,
    val special: Boolean = false,
    val cancelled: Boolean = false,
)
