package com.example.conexionapi

import retrofit2.Call
import retrofit2.http.*

interface SamirService {

    @POST("inmuebles/")
    fun postMyData(@Body data: SamirResponse): Call<SamirResponse>

    @DELETE("inmuebles/")
    fun deleteInmuebleByTitle(@Query("idInmueble") idInmueble: Int): Call<Void>

    @DELETE
    fun deleteInmueble(@Url url:String): Call<Void>

    @POST
    fun putInmueble(@Url url:String, @Body samirClass: SamirResponse): Call<SamirResponse>

    @GET("inmuebles/")
    fun getInmuebles(): Call<List<SamirResponse>>

}