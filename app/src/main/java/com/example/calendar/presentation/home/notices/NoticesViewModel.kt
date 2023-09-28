package com.example.calendar.presentation.home.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.example.calendar.data.local.DateEventsEntity
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.mappers.toDateEventsOnlyNotices
import com.example.calendar.data.mappers.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NoticesViewModel @Inject constructor(
    pager: Pager<Int, DateEventsEntity>
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val noticePagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toDateEventsOnlyNotices().events
            }
                .flatMap {
                    it
                }
        }
        .cachedIn(viewModelScope)
}