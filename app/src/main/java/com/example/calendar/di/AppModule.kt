package com.example.calendar.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.calendar.core.Constants
import com.example.calendar.data.local.EventDatabase
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.remote.EventApi
import com.example.calendar.data.remote.EventRemoteMediator
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideEventDatabase(@ApplicationContext context: Context): EventDatabase {
        return Room.databaseBuilder(
            context,
            EventDatabase::class.java,
            "events.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideEventApi(): EventApi {
        return Retrofit.Builder()
            .baseUrl(EventApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideEventPager(eventDb: EventDatabase, eventApi: EventApi): Pager<Int, EventEntity> {
        return Pager(
            config = PagingConfig(pageSize = Constants.MAX_RESULTS),
            remoteMediator = EventRemoteMediator(eventDb, eventApi),
            pagingSourceFactory = { eventDb.dao.pagingSource() }
        )
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(@ApplicationContext context: Context): GoogleAuthUiClient {
        val googleAuthUiClient by lazy {
            GoogleAuthUiClient(Identity.getSignInClient(context))
        }
        return googleAuthUiClient
    }


}