package com.example.medication.features.searchmedication.domain.repositories

import com.example.medication.features.searchmedication.domain.entities.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun searchMedicines(query: String): Flow<List<Medication>>
    suspend fun ensureCacheLoaded()
    suspend fun getMedicineById(id: String): Medication?
    suspend fun clearCache()
}