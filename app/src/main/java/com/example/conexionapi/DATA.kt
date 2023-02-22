package com.example.conexionapi

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class DATA(
    @SerializedName("meta"     ) var meta     : Meta?     = Meta(),
    @SerializedName("response" ) var response : Response? = Response()
)

data class Meta (

    @SerializedName("code" ) var code : Int? = null

)

data class Response (

    @SerializedName("holidays" ) var holidays : ArrayList<Holidays> = arrayListOf()

)

data class Holidays (

    @SerializedName("name"        ) var name        : String?           = null,
    @SerializedName("description" ) var description : String?           = null,
    @SerializedName("date"        ) var date        : Date?             = Date(),
    @SerializedName("type"        ) var type        : ArrayList<String> = arrayListOf()

)