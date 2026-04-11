package com.example.medication.features.medication.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication

interface MedicationRepository {
    suspend fun getMedications(): List<Medication>
    suspend fun getMedicationById(id: String): Medication
    suspend fun createMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String?  // ← sin = null en interfaz
    )
    suspend fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String?  // ← sin = null en interfaz
    ): Medication
    suspend fun deleteMedication(id: String)
}