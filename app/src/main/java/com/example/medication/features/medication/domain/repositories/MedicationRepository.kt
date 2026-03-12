package com.example.medication.features.medication.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication
import kotlinx.coroutines.flow.StateFlow

interface MedicationRepository {
    val medications: StateFlow<List<Medication>>
    suspend fun post(newMedication: Medication): Result<String>
    suspend fun update(modifiedMedication: Medication, index: UInt): Result<String>
    suspend fun delete(index: UInt): Result<String>
    suspend fun getById(id: String): Result<Medication>
}