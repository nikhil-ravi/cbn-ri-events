package com.example.calendar.presentation.home.notices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.mappers.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class NoticesViewModel @Inject constructor(
    pager: Pager<Int, EventEntity>
) : ViewModel() {
    val noticePagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toEvent()
            }
                .filter { event ->
                    event.description.cancelled || event.description.is_notice
                }
        }
        .cachedIn(viewModelScope)
}