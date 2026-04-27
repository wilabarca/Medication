package com.example.medication.features.history.domain.repositories

import com.example.medication.features.history.domain.entities.MedicationHistory

interface MedicationHistoryRepository {
    suspend fun saveToHistory(medication: MedicationHistory)
    suspend fun getHistory(patientId: String): List<MedicationHistory>
    suspend fun deleteFromHistory(id: String)
    suspend fun clearHistory(patientId: String)
}