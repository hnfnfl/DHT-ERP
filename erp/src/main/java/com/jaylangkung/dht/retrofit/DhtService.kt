package com.jaylangkung.dht.retrofit

import com.jaylangkung.dht.retrofit.response.InquiriesProcessResponse
import com.jaylangkung.dht.retrofit.response.InquiriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface DhtService {
    @GET("dht/getPersentaseProses")
    fun getPersentaseProses(
        @Header("Authorization") tokenAuth: String,
    ): Call<InquiriesProcessResponse>

    @GET("dht/getInquiries")
    fun getInquiries(
        @Header("Authorization") tokenAuth: String,
    ): Call<InquiriesResponse>
}