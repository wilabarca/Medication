package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.repositories.MedicationRepository

class DeleteMedicationUseCase(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(index: UInt): Result<String> {
        return repository.delete(index)
    }
}