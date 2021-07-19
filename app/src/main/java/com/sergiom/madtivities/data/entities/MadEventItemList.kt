package com.sergiom.madtivities.data.entities

import com.google.gson.annotations.SerializedName

data class MadEventItemList (
    @SerializedName("@graph") val eventsList: List<MadEventItem>
    )