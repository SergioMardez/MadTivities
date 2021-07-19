package com.sergiom.madtivities.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class MadEventItemDataBase(
    val id: String,
    val title: String,
    val description: String,
    val free: Int, //If 1 free
    val price: String,
    val dtstart: String,
    val dtend: String,
    val time: String,
    val excludedDays: String,
    @PrimaryKey val uid: String,
    val link: String,
    val eventLocation: String,
    val latitude: Float,
    val longitude: Float,
    val organizationName: String,
    val locality: String,
    val postalCode: String,
    val streetAddress: String,
    var favourite: Int = 0
)
