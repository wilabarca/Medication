package com.example.medication.features.searchmedicines.domain.repositories

import com.example.medication.features.searchmedicines.domain.entities.Medicine
import kotlinx.coroutines.flow.Flow

interface MedicineRepository {
    fun searchMedicines(query: String): Flow<List<Medicine>>
    suspend fun getMedicineById(id: Int): Medicine?
    suspend fun clearCache()
}