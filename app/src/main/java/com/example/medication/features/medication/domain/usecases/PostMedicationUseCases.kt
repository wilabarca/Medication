package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class PostMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(
        patientId: String,        // ← userId → patientId
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean = true,
        startDate: String? = null, // ← nuevo
        endDate: String? = null,   // ← nuevo
        photoPath: String? = null,
        deviceId: String
    ) {
        repository.createMedication(
            patientId    = patientId,
            name         = name,
            dosage       = dosage,
            form         = form,
            instructions = instructions,
            notes        = notes,
            quantity     = quantity,
            price        = price,
            isActive     = isActive,
            startDate    = startDate,
            endDate      = endDate,
            photoPath    = photoPath,
            deviceId     = deviceId
        )
    }
}