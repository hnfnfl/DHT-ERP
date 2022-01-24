package com.jaylangkung.dht.retrofit

import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
import com.jaylangkung.dht.retrofit.response.AdminResponse
import com.jaylangkung.dht.retrofit.response.MenuResponse
import retrofit2.Call
import retrofit2.http.*

interface DataService {
    @FormUrlEncoded
    @POST("main/getMenu")
    fun getMenu(
        @Field("idlevel") idlevel: String,
        @Header("Authorization") tokenAuth: String
    ): Call<MenuResponse>

    @FormUrlEncoded
    @POST("main/insertWebApp")
    fun insertWebApp(
        @Field("idadmin") idadmin: String,
        @Field("device_id") device_id: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @GET("main/getAllAdmin")
    fun getAllAdmin(
        @Header("Authorization") tokenAuth: String,
    ): Call<AdminResponse>

    @GET("main/getLevel")
    fun getLevel(
        @Header("Authorization") tokenAuth: String,
    ): Call<AdminResponse>

    @GET("main/getDepartemen")
    fun getDepartemen(
        @Header("Authorization") tokenAuth: String,
    ): Call<AdminResponse>
}