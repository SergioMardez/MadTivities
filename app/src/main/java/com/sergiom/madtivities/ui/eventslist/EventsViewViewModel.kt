package com.sergiom.madtivities.ui.eventslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.data.repository.EventsRepository
import com.sergiom.madtivities.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {
    val events = repository.getEvents()
    val favourites = repository.getFavouriteEvents()

    fun saveFavourite(event: MadEventItemDataBase) {
        if (event.favourite == 0) {
            event.favourite = 1
        } else {
            event.favourite = 0
        }
        viewModelScope.launch {
            repository.saveEvent(event)
        }
    }
}