package com.example.medication.features.auth.data.dataresources.remote.api

import com.example.medication.features.auth.data.dataresources.remote.models.*
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {

    @POST("users/")
    suspend fun register(
        @Body request : RegisterRequest
    ): UserDto

    @POST("users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body request: UpdateUserRequest
    ): LoginResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @Path("id") id: String
    )

    @POST("devices/register")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): BasicMessageResponse
}