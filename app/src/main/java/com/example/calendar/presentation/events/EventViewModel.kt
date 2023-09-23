package com.example.calendar.presentation.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.mappers.toEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    pager: Pager<Int, EventEntity>
) : ViewModel() {
    val eventPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toEvent()
            }
        }
        .cachedIn(viewModelScope)

// I created this to add a sticky header for each date, but the lazy column doesn't support sticky headers
// inside the "items" scope, so I had to use a different approach
//    val eventPagingFlow = pager
//        .flow
//        .map { pagingData: PagingData<EventEntity> ->
//            pagingData.map {
//                EventOrSeparator.EventItem(it.toEvent())
//            }
//            .insertSeparators {
//                before, after ->
//                when{
//                    before == null && after != null -> EventOrSeparator.SeparatorItem(
//                        date=ZonedDateTime.parse(after.event.startDateTime).toLocalDate().toString()
//                    )
//                    after == null -> null
//                    before != null && showIndicator(before.event.startDateTime!!, after.event.startDateTime!!) ->
//                        EventOrSeparator.SeparatorItem(
//                            ZonedDateTime.parse(after.event.startDateTime).toLocalDate().toString()
//                        )
//                    else -> null
//                }
//            }
//        }
//        .cachedIn(viewModelScope)
}

// I created this to add a sticky header for each date, but the lazy column doesn't support sticky headers
// inside the "items" scope, so I had to use a different approach
fun showIndicator(beforestartDateTime: String, afterstartDateTime: String): Boolean {
    val beforeDate = ZonedDateTime.parse(beforestartDateTime).toLocalDate()
    val afterDate = ZonedDateTime.parse(afterstartDateTime).toLocalDate()
    return beforeDate != afterDate
}