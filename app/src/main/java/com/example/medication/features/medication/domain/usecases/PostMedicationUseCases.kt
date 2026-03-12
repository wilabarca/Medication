package com.example.medication.features.medication.domain.usecases


import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository

class PostMedicationUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(name: String, description: String, quantity: Int, price: Double): Result<String> {
        val newMedication = Medication(
            id = java.util.UUID.randomUUID().toString(),
            name = name,
            description = description,
            quantity = quantity,
            price = price
        )
        return repository.post(newMedication)
    }
}