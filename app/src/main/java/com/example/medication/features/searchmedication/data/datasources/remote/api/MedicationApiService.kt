package com.example.medication.features.searchmedication.data.datasources.remote.api

import com.example.medication.features.searchmedication.data.datasources.remote.models.MedicationDto
import retrofit2.http.GET

interface MedicationApiService {

    @GET("medications")
    suspend fun getAllMedicines(): List<MedicationDto>
}