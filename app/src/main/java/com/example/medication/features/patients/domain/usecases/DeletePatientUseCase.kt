package com.example.medication.features.patients.domain.usecases

import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class DeletePatientUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deletePatient(id)
    }
}