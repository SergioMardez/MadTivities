package com.sergiom.madtivities.ui.eventdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.data.repository.EventsRepository
import com.sergiom.madtivities.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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
}