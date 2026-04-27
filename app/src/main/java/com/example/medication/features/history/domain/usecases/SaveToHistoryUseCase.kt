// SaveToHistoryUseCase.kt
package com.example.medication.features.history.domain.usecases

import com.example.medication.features.history.domain.entities.MedicationHistory
import com.example.medication.features.history.domain.repositories.MedicationHistoryRepository
import javax.inject.Inject

class SaveToHistoryUseCase @Inject constructor(
    private val repository: MedicationHistoryRepository
) {
    suspend operator fun invoke(medication: MedicationHistory) {
        repository.saveToHistory(medication)
    }
}