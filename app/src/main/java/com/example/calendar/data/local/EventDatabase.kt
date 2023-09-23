package com.example.calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EventEntity::class, NextPageTokenEntity::class], version = 5)
abstract class EventDatabase: RoomDatabase() {
    abstract val dao: EventDao
    abstract val nextPageTokenDao: NextPageTokenDao
}