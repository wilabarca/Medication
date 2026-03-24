package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class PostMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ) {
        repository.createMedication(
            name = name,
            description = description,
            quantity = quantity,
            price = price
        )
    }
}