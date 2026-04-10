package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class UpdateMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String? = null  // ← agregar
    ): Medication {
        return repository.updateMedication(
            id = id,
            name = name,
            description = description,
            quantity = quantity,
            price = price,
            photoPath = photoPath  // ← agregar
        )
    }
}