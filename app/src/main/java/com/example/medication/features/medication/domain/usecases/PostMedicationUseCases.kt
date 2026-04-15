package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class PostMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(
        userId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean = true,
        photoPath: String? = null
    ) {
        repository.createMedication(
            userId = userId,
            name = name,
            dosage = dosage,
            form = form,
            instructions = instructions,
            notes = notes,
            quantity = quantity,
            price = price,
            isActive = isActive,
            photoPath = photoPath
        )
    }
}