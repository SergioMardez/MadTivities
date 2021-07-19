package com.sergiom.madtivities.data.remote

import javax.inject.Inject

class EventRemoteDataSource @Inject constructor(
    private val eventService: EventService
): BaseDataSource() {
    suspend fun getEvents() = getResult { eventService.getAllEvents() }
}