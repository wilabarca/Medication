package com.example.medication.features.patients.data.dataresources.remote.api

import com.example.medication.features.patients.data.dataresources.remote.models.CreatePatientRequest
import com.example.medication.features.patients.data.dataresources.remote.models.LinkAccountRequest
import com.example.medication.features.patients.data.dataresources.remote.models.LinkAccountResponse
import com.example.medication.features.patients.data.dataresources.remote.models.PatientDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PatientsApi {

    @POST("patients")
    suspend fun createPatient(
        @Body body: CreatePatientRequest
    ): PatientDto

    @GET("patients")
    suspend fun getPatientsByCaregiver(
        @Query("caregiverUserId") caregiverUserId: String
    ): List<PatientDto>

    @GET("patients/{id}")
    suspend fun getPatientById(
        @Path("id") id: String
    ): PatientDto

    @PUT("patients/{id}")
    suspend fun updatePatient(
        @Path("id") id: String,
        @Body patient: PatientDto
    ): PatientDto

    @DELETE("patients/{id}")
    suspend fun deletePatient(
        @Path("id") id: String
    )

    @POST("patients/link-account")
    suspend fun linkAccount(
        @Body body: LinkAccountRequest
    ): LinkAccountResponse
}