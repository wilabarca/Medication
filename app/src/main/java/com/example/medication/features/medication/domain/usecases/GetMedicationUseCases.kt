package com.example.medication.features.medication.domain.usecases

import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(  // ← agrega la 's'
    private val repository: MedicationRepository
) {
    suspend operator fun invoke(patientId: String): List<Medication> {  // ← agrega el parámetro
        return repository.getMedications(patientId)
    }
}