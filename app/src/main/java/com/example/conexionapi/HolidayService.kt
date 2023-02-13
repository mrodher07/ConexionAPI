package com.example.conexionapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidayService {
    @GET("holidays")
    fun getHolidays(
        @Query("1a14837dd82dc020d405b6cd2d9f99888dd05e75") apiKey:String,
        @Query("page") page: Int
    ) : Call<HolidaysResponse>
}