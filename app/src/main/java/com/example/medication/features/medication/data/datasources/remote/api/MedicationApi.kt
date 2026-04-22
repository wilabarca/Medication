package com.example.medication.features.medication.data.datasources.remote.api

import com.example.medication.features.medication.data.datasources.remote.models.CreateMedicationRequest
import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.medication.data.datasources.remote.models.MedicationResponse
import com.example.medication.features.medication.data.datasources.remote.models.MessageResponse
import com.example.medication.features.medication.data.datasources.remote.models.UpdateMedicationRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MedicationApi {

    @GET("medications")
    suspend fun getMedications(
        @Query("patientId") patientId: String  // ← agrega esto
    ): List<MedicationDto>

    @GET("medications/{id}")
    suspend fun getMedicationById(@Path("id") id: String): MedicationDto

    @POST("medications")
    suspend fun createMedication(
        @Body body: CreateMedicationRequest
    ): MedicationResponse

    @PUT("medications/{id}")
    suspend fun updateMedication(
        @Path("id") id: String,
        @Body body: UpdateMedicationRequest
    ): MedicationResponse

    @DELETE("medications/{id}")
    suspend fun deleteMedication(@Path("id") id: String): MessageResponse
}