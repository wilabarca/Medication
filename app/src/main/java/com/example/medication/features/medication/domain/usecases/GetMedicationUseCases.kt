package com.example.medication.features.medication.domain.usecases


import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetMedicationUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(): List<Medication> {
        return repository.getMedications()
    }
}