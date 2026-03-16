package com.example.medication.features.medication.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication
import kotlinx.coroutines.flow.StateFlow

interface MedicationRepository {
    suspend fun getMedications(): List<Medication>
    suspend fun getMedicationById(id: String): Medication
    suspend fun createMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ): Medication

    suspend fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ): Medication

    suspend fun deleteMedication(id: String)
}