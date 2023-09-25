package com.example.calendar.presentation.home.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.calendar.data.firestore.addEventToUserFavorites
import com.example.calendar.data.firestore.getFavoritesFromFirestore
import com.example.calendar.data.firestore.removeEventFromUserFavorites
import com.example.calendar.data.local.EventEntity
import com.example.calendar.data.mappers.toEvent
import com.example.calendar.presentation.auth.GoogleAuthUiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    pager: Pager<Int, EventEntity>,
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {
    val eventPagingFlow = pager
        .flow
        .map { pagingData ->
            pagingData.map {
                it.toEvent()
            }
        }
        .cachedIn(viewModelScope)

    private val _favoriteEvents = MutableStateFlow<List<String>>(emptyList<String>())
    val favoriteEvents: StateFlow<List<String>> = _favoriteEvents.asStateFlow()

    init {
        loadFavorites()
    }

    fun addFavorite(eventId: String) {
        val userId = googleAuthUiClient.getSignedInUser()?.userId
        if (userId != null) {
            val isCurrentlyFavorited = _favoriteEvents.value.contains(eventId)
            // Perform the toggle optimistically
            _favoriteEvents.value = if (isCurrentlyFavorited) {
                // Remove the event from favorites
                _favoriteEvents.value - eventId
            } else {
                // Add the event to favorites
                _favoriteEvents.value + eventId
            }
            viewModelScope.launch {
                try {
                    if (isCurrentlyFavorited) {
                        // If it was favorited, remove it from user favorites in the backend
                        removeEventFromUserFavorites(userId, eventId)
                    } else {
                        // If it wasn't favorited, add it to user favorites in the backend
                        addEventToUserFavorites(userId, eventId)
                    }
                    // No need to update UI since we've already done the optimistic update
                } catch (e: Exception) {
                    // Handle the error (e.g., network issue, write failure)
                    // Revert the UI update by loading favorites again
                    loadFavorites()
                    // You can also log or show a message about the error
                    Log.e("EventViewModel", "Failed to update favorites: ${e.message}")
                }
            }
        }
    }

    private fun loadFavorites() {
        val userId = googleAuthUiClient.getSignedInUser()?.userId
        if (userId != null) {
            viewModelScope.launch {
                val favorites = getFavoritesFromFirestore(userId)
                _favoriteEvents.value = favorites
            }
        }
    }

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