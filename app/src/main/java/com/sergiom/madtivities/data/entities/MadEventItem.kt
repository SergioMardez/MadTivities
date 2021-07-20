package com.sergiom.madtivities.data.entities

import com.google.gson.annotations.SerializedName

data class MadEventItem(
    //val @id: String,
    //val @type: String,
    val id: String,
    val title: String,
    val description: String,
    val free: Int, //If 1 free
    val price: String,
    val dtstart: String,
    val dtend: String,
    val time: String,
    @SerializedName("excluded-days") val excludedDays: String,
    val recurrence: Recurrence,
    val uid: String,
    val link: String,
    @SerializedName("event-location") val eventLocation: String,
    val address: EventAddress,
    val location: EventLocation,
    val organization: EventOrganization,
    var favourite: Int = 0
) {

    data class Recurrence(
        val days: String,
        val frequency: String,
        val interval: Int
    )

    data class EventLocation (
        val latitude: Double,
        val longitude: Double
    )

    data class EventOrganization(
        @SerializedName("organization-name") val organizationName: String,
        val accesibility: String
    )

    data class EventAddress (
        val area: EventArea
    )

    data class EventArea (
        @SerializedName("@id")val id: String,
        val locality: String,
        @SerializedName("postal-code")val postalCode: String,
        @SerializedName("street-address")val streetAddress: String
    )
}
