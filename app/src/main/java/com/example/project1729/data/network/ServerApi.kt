package com.example.project1729.data.network

import com.example.project1729.data.model.LoginRequest
import com.example.project1729.data.model.LoginResponse
import com.example.project1729.data.model.MeasurementRequest
import com.example.project1729.data.model.MeasurementResponse
import com.example.project1729.data.model.MeasurementsResponse
import com.example.project1729.data.model.RegisterResponse
import com.example.project1729.data.model.SignupRequest
import com.example.project1729.domain.model.Measurement
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServerApi {
    @POST("auth/login")
    suspend fun postLogin(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/signup")
    suspend fun post(@Body request: SignupRequest): Response<RegisterResponse>

    @POST("measurements/add-results")
    suspend fun addMeasurement(@Body request: MeasurementRequest): Response<MeasurementResponse>

    @GET("measurements/get-results")
    suspend fun getMeasurements(@Query("userID") userId: Int, @Query("type") type: String): Response<MeasurementsResponse>
}