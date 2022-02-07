package com.jaylangkung.dht.retrofit

import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
import com.jaylangkung.dht.retrofit.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @GET("main/getSpinnerData")
    fun getSpinnerData(
        @Header("Authorization") tokenAuth: String,
    ): Call<DataSpinnerResponse>

    @GET("main/getLevel")
    fun getLevel(
        @Header("Authorization") tokenAuth: String,
    ): Call<LevelResponse>

    @FormUrlEncoded
    @POST("main/updatePassword")
    fun updatePassword(
        @Field("idadmin") idadmin: String,
        @Field("password") password: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @Multipart
    @POST("main/updateProfile")
    fun updateProfile(
        @Part("idadmin") idadmin: RequestBody,
        @Part("email") email: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part("telp") telp: RequestBody,
        @Part("idlevel") idlevel: RequestBody,
        @Part("iddepartemen") iddepartemen: RequestBody,
        @Part filefoto: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/deleteSecurity")
    fun deleteSecurity(
        @Field("idadmin") idadmin: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/deleteAdmin")
    fun deleteAdmin(
        @Field("idadmin") idadmin: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/getHakAkses")
    fun getHakAkses(
        @Field("idlevel") idlevel: String,
        @Header("Authorization") tokenAuth: String
    ): Call<LevelResponse>

    @FormUrlEncoded
    @POST("main/insertLevel")
    fun insertLevel(
        @Field("level") level: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/updateLevel")
    fun updateLevel(
        @Field("idlevel") idlevel: String,
        @Field("level") level: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("main/deleteLevel")
    fun deleteLevel(
        @Field("idlevel") idlevel: String,
        @Header("Authorization") tokenAuth: String
    ): Call<DefaultResponse>

    @GET("main/getCustomer")
    fun getCustomer(
        @Header("Authorization") tokenAuth: String,
    ): Call<CustomerResponse>

    @GET("main/getSupplier")
    fun getSupplier(
        @Header("Authorization") tokenAuth: String,
    ): Call<SupplierResponse>

    @GET("main/getProduct")
    fun getProduct(
        @Header("Authorization") tokenAuth: String,
    ): Call<ProductResponse>

    @GET("main/getShipment")
    fun getShipment(
        @Header("Authorization") tokenAuth: String,
    ): Call<ShipmentResponse>

    @GET("main/getBarang")
    fun getBarang(
        @Header("Authorization") tokenAuth: String,
    ): Call<GoodsResponse>
}