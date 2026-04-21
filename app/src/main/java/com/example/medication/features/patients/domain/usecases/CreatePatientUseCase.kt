package com.example.medication.features.patients.domain.usecases

import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class CreatePatientUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(
        caregiverUserId: String,
        linkedUserId: String?,
        name: String,
        birthDate: String?,
        relationship: String?,
        notes: String?,
        isActive: Boolean = true
    ): Patient {
        return repository.createPatient(
            caregiverUserId = caregiverUserId,
            linkedUserId = linkedUserId,
            name = name,
            birthDate = birthDate,
            relationship = relationship,
            notes = notes,
            isActive = isActive
        )
    }
}