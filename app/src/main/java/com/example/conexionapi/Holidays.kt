package com.example.conexionapi

import com.google.gson.annotations.SerializedName
import java.util.Date

data class HolidaysResponse (val holidays: List<Holidays>)

data class Holidays(
    @SerializedName("name"          ) var name         : String?           = null,
    @SerializedName("description"   ) var description  : String?           = null,
    @SerializedName("date"          ) var date         : Date?             = Date(),
    @SerializedName("primary_type"  ) var primaryType  : String?           = null,
    @SerializedName("locations"     ) var locations    : String?           = null,
)