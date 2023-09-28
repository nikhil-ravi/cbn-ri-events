package com.example.calendar.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EventDao {

    @Upsert
    suspend fun upsertAll(events: List<DateEventsEntity>)

    @Query("SELECT * FROM dateeventsentity order by date asc")
    fun pagingSource(): PagingSource<Int, DateEventsEntity>

    @Query("DELETE FROM dateeventsentity")
    suspend fun clearAll()

}