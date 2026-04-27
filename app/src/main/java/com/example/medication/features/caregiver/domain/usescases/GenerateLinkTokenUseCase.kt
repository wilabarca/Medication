package com.example.medication.features.caregiver.domain.usecases

import com.example.medication.features.caregiver.domain.repositories.CaregiverRepository
import javax.inject.Inject

class GenerateLinkTokenUseCase @Inject constructor(
    private val repository: CaregiverRepository
) {
    suspend operator fun invoke(caregiverId: String): String {
        return repository.generateLinkToken(caregiverId)
    }
}