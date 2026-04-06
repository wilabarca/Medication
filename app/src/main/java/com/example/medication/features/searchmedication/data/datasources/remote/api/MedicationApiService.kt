package com.example.medication.features.searchmedication.data.remote.api

import com.example.medication.features.searchmedication.data.remote.dto.MedicationDto
import retrofit2.http.GET

interface MedicationApiService {

    @GET("medications")   // ✅ endpoint correcto de tu API
    suspend fun getAllMedicines(): List<MedicationDto>
}