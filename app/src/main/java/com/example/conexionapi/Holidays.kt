package com.example.conexionapi

import com.google.gson.annotations.SerializedName

data class HolidaysResponse (
    @SerializedName("holidays") var holidays : ArrayList<Holidays> = arrayListOf()
)
data class Meta (
    @SerializedName("code" ) var code : Int? = null
)

data class Holidays(
    @SerializedName("meta"     ) var meta     : Meta?     = Meta(),
    @SerializedName("response" ) var response : HolidaysResponse? = HolidaysResponse()
)