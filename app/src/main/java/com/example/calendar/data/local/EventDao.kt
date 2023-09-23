package com.example.calendar.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface EventDao {

    @Upsert
    suspend fun upsertAll(events: List<EventEntity>)

    @Query("SELECT * FROM evententity order by startDateTime asc")
    fun pagingSource(): PagingSource<Int, EventEntity>

    @Query("DELETE FROM evententity")
    suspend fun clearAll()

}