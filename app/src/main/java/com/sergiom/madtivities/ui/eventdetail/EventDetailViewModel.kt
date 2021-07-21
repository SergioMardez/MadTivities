package com.sergiom.madtivities.ui.eventdetail

import androidx.lifecycle.*
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.data.repository.EventsRepository
import com.sergiom.madtivities.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventsRepository
) : ViewModel() {

    private val uid = MutableLiveData<String>()
    private val _event = uid.switchMap { id ->
        repository.getEvent(id)
    }

    val event: LiveData<Resource<MadEventItemDataBase>> = _event

    fun start(id: String) {
        uid.value = id
    }

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