package com.example.medication.features.searchmedication.domain.usecases

import com.example.medication.features.searchmedication.domain.repositories.MedicationRepository
import javax.inject.Inject

// Igual que SyncPostsUseCase del profe
class SyncMedicationsUseCase @Inject constructor(
    private val repository: MedicationRepository
) {
    suspend operator fun invoke() {
        repository.syncMedications()
    }
}