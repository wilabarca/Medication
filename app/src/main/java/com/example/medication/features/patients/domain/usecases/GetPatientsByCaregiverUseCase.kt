package com.example.medication.features.patients.domain.usecases

import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class GetPatientsByCaregiverUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(
        caregiverUserId: String
    ): List<Patient> {
        return repository.getPatientsByCaregiver(caregiverUserId)
    }
}