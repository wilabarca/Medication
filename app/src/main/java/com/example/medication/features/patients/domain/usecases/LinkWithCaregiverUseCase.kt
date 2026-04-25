package com.example.medication.features.patients.domain.usecases

import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class LinkWithCaregiverUseCase @Inject constructor(
    private val repository: PatientRepository
) {
    suspend operator fun invoke(token: String, userId: String) {
        repository.linkAccount(token = token, userId = userId)
    }
}