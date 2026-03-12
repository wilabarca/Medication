package com.example.medication.features.medication.domain.usecases


import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import kotlinx.coroutines.flow.StateFlow

class GetMedicationUseCase(
    private val repository: MedicationRepository
) {
    operator fun invoke(): StateFlow<List<Medication>> {
        return repository.medications
    }
}