package com.jaylangkung.dht.retrofit

import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
import com.jaylangkung.dht.retrofit.response.MenuResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface DataService {
    @FormUrlEncoded
    @POST("main/getMenu")
    fun getMenu(
        @Field("idlevel") idlevel: String,
        @Header("Authorization") tokenAuth: String
    ): Call<MenuResponse>

}