package com.jaylangkung.dht.retrofit

import com.jaylangkung.dht.retrofit.response.*
import retrofit2.Call
import retrofit2.http.*

interface DhtService {
    @GET("dht/getPersentaseProses")
    fun getPersentaseProses(
        @Header("Authorization") tokenAuth: String,
    ): Call<InquiriesProcessResponse>

    @GET("dht/getDepartemen")
    fun getDepartemen(
        @Header("Authorization") tokenAuth: String,
    ): Call<DepartmentResponse>

    @GET("dht/getInquiries")
    fun getInquiries(
        @Header("Authorization") tokenAuth: String,
    ): Call<InquiriesResponse>

    @FormUrlEncoded
    @POST("dht/updateInquiries")
    fun updateInquiries(
        @Field("idinquiries") idinquiries: String,
        @Field("status") status: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("dht/getWorkOrderBreakdown")
    fun getWorkOrderBreakdown(
        @Field("idproduk") idproduk: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @GET("dht/getPackingBarcode")
    fun getPackingBarcode(
        @Header("Authorization") tokenAuth: String,
    ): Call<PackingResponse>

    @FormUrlEncoded
    @POST("dht/updatePackingBarcode")
    fun updatePackingBarcode(
        @Field("kode") kode: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>
}