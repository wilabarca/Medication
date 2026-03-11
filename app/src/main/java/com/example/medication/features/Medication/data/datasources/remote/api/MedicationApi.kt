package com.example.medication.features.Medication.data.datasources.remote.api

import com.example.medication.features.Medication.data.datasources.remote.models.MedicationDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MedicationApi {

    @GET("medications")
    suspend fun getMedications(): List<MedicationDto>

    @GET("medications/{id}")
    suspend fun getMedicationById(@Path("id") id: String): MedicationDto

    @POST("medications")
    suspend fun postMedication(@Body medication: MedicationDto): MedicationDto

    @PUT("medications/{index}")
    suspend fun updateMedication(
        @Path("index") index: UInt,
        @Body medication: MedicationDto
    ): MedicationDto

    @DELETE("medications/{index}")
    suspend fun deleteMedication(@Path("index") index: UInt)
}