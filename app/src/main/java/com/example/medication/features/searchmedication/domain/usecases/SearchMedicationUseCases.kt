package com.example.medication.features.searchmedication.domain.usecases

import javax.inject.Inject

// Wrapper que agrupa todos los use cases
data class SearchMedicationUseCases @Inject constructor(
    val searchMedication: SearchMedicationUseCase,
    val syncMedications: SyncMedicationsUseCase
)