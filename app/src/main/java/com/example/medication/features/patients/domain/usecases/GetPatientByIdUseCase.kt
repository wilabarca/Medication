package com.example.medication.features.patients.domain.usecases

import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class GetPatientByIdUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(id: String): Patient {
        return repository.getPatientById(id)
    }
}