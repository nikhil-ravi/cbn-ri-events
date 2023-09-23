package com.example.calendar.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventApi {

    @GET("calendar/v3/calendars/{calendarId}/events")
    suspend fun getEvents(
        @Path("calendarId") calendarId: String,
        @Query("maxResults") maxResults: Int,
        @Query("orderBy") orderBy: String,
        @Query("showDeleted") showDeleted: Boolean,
        @Query("singleEvents") singleEvents: Boolean,
        @Query("timeMin") timeMin: String,
        @Query("fields") fields: String,
        @Query("timeMax") timeMax: String?,
        @Query("pageToken") pageToken: String?,
        @Query("key") key: String
    ): EventListDto

    companion object{
        const val BASE_URL = "https://www.googleapis.com/"
    }
}