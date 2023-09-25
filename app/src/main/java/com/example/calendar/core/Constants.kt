package com.example.calendar.core

object Constants {

    const val MAX_RESULTS = 1000

    const val ORDER_BY = "startTime"

    const val SHOW_DELETED = false

    const val SINGLE_EVENTS = true

    const val FIELDS =
        "nextPageToken,nextSyncToken,items(id,summary,description,start,end,location,status,recurringEventId)"

}