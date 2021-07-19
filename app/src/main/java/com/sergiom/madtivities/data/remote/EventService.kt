package com.sergiom.madtivities.data.remote

import com.sergiom.madtivities.data.entities.MadEventItemList
import retrofit2.Response
import retrofit2.http.GET

interface EventService {
    @GET("catalogo/206974-0-agenda-eventos-culturales-100.json")
    suspend fun getAllEvents() : Response<MadEventItemList>
}