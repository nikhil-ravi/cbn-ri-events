package com.example.calendar.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NextPageTokenDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(nextPageTokenEntity: NextPageTokenEntity)

    @Query("SELECT * FROM nextpagetokenentity")
    suspend fun getNextPageToken(): NextPageTokenEntity?

    @Query("DELETE FROM nextpagetokenentity")
    suspend fun clearAll()

}