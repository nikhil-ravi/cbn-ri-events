package com.example.calendar.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.calendar.BuildConfig
import com.example.calendar.data.local.EventDatabase
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.local.NextPageTokenEntity
import com.example.calendar.data.mappers.toEventEntity
import com.example.calendar.core.Constants
import retrofit2.HttpException
import java.io.IOException
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator(
    private val eventDb: EventDatabase,
    private val eventApi: EventApi,
) : RemoteMediator<Int, EventEntity>() {

    private val eventDao = eventDb.dao
    private val nextPageTokenDao = eventDb.nextPageTokenDao

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EventEntity>
    ): MediatorResult {
        return try {

            val loadPageToken = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {

                    val nextPageToken = eventDb.withTransaction {
                        nextPageTokenDao.getNextPageToken()
                    }

                    if (nextPageToken?.nextPageToken == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    nextPageToken.nextPageToken
                }
            }

            val events = eventApi.getEvents(
                calendarId = BuildConfig.CALENDAR_ID,
                maxResults = state.config.pageSize,
                orderBy = Constants.ORDER_BY,
                showDeleted = Constants.SHOW_DELETED,
                singleEvents = Constants.SINGLE_EVENTS,
                timeMin = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                fields = Constants.FIELDS,
                timeMax = null,
                pageToken = loadPageToken,
                key = BuildConfig.GC_API_KEY
            )

            eventDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    nextPageTokenDao.clearAll()
                    eventDao.clearAll()
                }

                nextPageTokenDao.insertOrReplace(
                    NextPageTokenEntity(nextPageToken = events.nextPageToken)
                )

                val eventEntities = events.items.map { it.toEventEntity() }
                eventDb.dao.upsertAll(eventEntities)
            }

            MediatorResult.Success(
                endOfPaginationReached = events.nextPageToken == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

}