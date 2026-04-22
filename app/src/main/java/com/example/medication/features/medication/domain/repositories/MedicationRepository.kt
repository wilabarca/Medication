package com.example.medication.features.medication.domain.repositories

import com.example.medication.features.medication.domain.entities.Medication

interface MedicationRepository {

    suspend fun getMedications(patientId: String): List<Medication>  // ← agrega patientId

    suspend fun getMedicationById(id: String): Medication

    suspend fun createMedication(
        patientId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        startDate: String? = null,
        endDate: String? = null,
        photoPath: String?,
        deviceId: String
    )

    suspend fun updateMedication(
        id: String,
        patientId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        startDate: String? = null,
        endDate: String? = null,
        photoPath: String?,
        deviceId: String
    ): Medication

    suspend fun deleteMedication(id: String)
}