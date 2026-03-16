package com.example.medication.features.medication.domain.usecases


import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class GetIDMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(id: String): Medication {
        return repository.getMedicationById(id)
    }
}