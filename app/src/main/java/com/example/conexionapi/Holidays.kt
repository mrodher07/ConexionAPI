package com.example.conexionapi

import java.util.Date

data class HolidaysResponse (val holidays: List<Holidays>)

data class Holidays(
    val name: String,
    val description: String,
    val date: Date,
    val locations: String,
    val primary_type: String
)