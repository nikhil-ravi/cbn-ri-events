package com.example.calendar.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(EntityConverters::class)
@Database(
    entities = [DateEventsEntity::class, NextPageTokenEntity::class],
    version = 9
)
abstract class EventDatabase : RoomDatabase() {
    abstract val dao: EventDao
    abstract val nextPageTokenDao: NextPageTokenDao
}