package com.example.medication.features.searchmedicines.data.remote.api

import com.example.medication.features.searchmedicines.data.remote.dto.MedicineDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MedicineApiService {

    @GET("medicines")
    suspend fun getAllMedicines(): List<MedicineDto>

    @GET("medicines/search")
    suspend fun searchMedicines(
        @Query("q") query: String
    ): List<MedicineDto>
}