package com.example.medication.features.medication.domain.usecases


import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository

class GetByIdMedicationUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(id: String): Result<Medication> {
        return repository.getById(id)
    }
}