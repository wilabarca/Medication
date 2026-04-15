package com.example.medication.features.medication.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication

interface MedicationRepository {

    suspend fun getMedications(): List<Medication>

    suspend fun getMedicationById(id: String): Medication

    suspend fun createMedication(
        userId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        photoPath: String?
    )

    suspend fun updateMedication(
        id: String,
        userId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        photoPath: String?
    ): Medication

    suspend fun deleteMedication(id: String)
}