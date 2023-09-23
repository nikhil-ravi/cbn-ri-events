package com.example.calendar.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nextpagetokenentity")
data class NextPageTokenEntity(
    @PrimaryKey
    val label: String = "nextPageToken",
    val nextPageToken: String? = null,
)
