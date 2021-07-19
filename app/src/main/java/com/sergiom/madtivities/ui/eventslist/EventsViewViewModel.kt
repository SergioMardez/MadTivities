package com.sergiom.madtivities.ui.eventslist

import androidx.lifecycle.ViewModel
import com.sergiom.madtivities.data.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsViewViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {
    val events = repository.getEvents()
}