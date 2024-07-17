package com.example.project1729.data.network

import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface SheetsSearchApi {
    @GET("exec")
    suspend fun get(): JsonObject

    @FormUrlEncoded
    @POST("exec")
    suspend fun post(
        @Field("fio") fio: String,
        @Field("year") year: String,
        @Field("diagnoz") diagnoz: String,
        @Field("login") login: String,
        @Field("password") password: String
    )
}