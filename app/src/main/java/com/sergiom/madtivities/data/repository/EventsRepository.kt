package com.sergiom.madtivities.data.repository

import com.sergiom.madtivities.data.entities.MadEventItem
import com.sergiom.madtivities.data.entities.MadEventItemDataBase
import com.sergiom.madtivities.data.local.EventsDao
import com.sergiom.madtivities.data.remote.EventRemoteDataSource
import com.sergiom.madtivities.utils.performGetItem
import com.sergiom.madtivities.utils.performGetOperation
import java.lang.NullPointerException
import javax.inject.Inject

class EventsRepository @Inject constructor(
    private val remoteDataSource: EventRemoteDataSource,
    private val localDataSource: EventsDao
) {
    fun getEvents() = performGetOperation(
        databaseQuery = { localDataSource.getAllEvents() },
        networkCall = { remoteDataSource.getEvents() },
        saveCallResult = { localDataSource.insertAll(mapEventsToDatabase(it.eventsList)) }
    )

    private fun mapEventsToDatabase(events: List<MadEventItem>): List<MadEventItemDataBase> {
        val dataBaseEvents = mutableListOf<MadEventItemDataBase>()

        for (event in events) {
            val dataBaseEvent = MadEventItemDataBase(
                event.id,
                event.title,
                event.description,
                event.free,
                event.price,
                event.dtstart,
                event.dtend,
                event.time,
                event.excludedDays,
                event.uid,
                event.link,
                event.eventLocation,
                try{event.location.latitude} catch (ex: NullPointerException) {null},
                try{event.location.longitude} catch (ex: NullPointerException) {null},
                try{event.organization.organizationName} catch (ex: NullPointerException) {""},
                try{event.address.area.locality} catch (ex: NullPointerException) {""},
                try{event.address.area.postalCode} catch (ex: NullPointerException) {""},
                try{event.address.area.streetAddress} catch (ex: NullPointerException) {""},
                0
            )
            dataBaseEvents.add(dataBaseEvent)
        }

        return dataBaseEvents
    }

    fun getEvent(uid: String) = performGetItem( databaseQuery = { localDataSource.getEvent(uid) })
}