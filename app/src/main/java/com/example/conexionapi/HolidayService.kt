package com.example.conexionapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayService {
    @GET("v2/holidays")
    fun getHolidays(
        @Query("api_key") api_key:String,
        @Query("country") country: String,
        @Query("year") year: String
    ) : Call<DATA>
}