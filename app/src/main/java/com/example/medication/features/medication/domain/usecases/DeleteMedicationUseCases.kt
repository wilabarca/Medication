package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class DeleteMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteMedication(id)
    }
}