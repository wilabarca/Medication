package com.example.medication.features.history.data.repositories

import com.example.medication.core.database.dao.MedicationHistoryDao
import com.example.medication.core.database.entities.MedicationHistoryEntity
import com.example.medication.features.history.domain.entities.MedicationHistory
import com.example.medication.features.history.domain.repositories.MedicationHistoryRepository
import javax.inject.Inject

class MedicationHistoryRepositoryImpl @Inject constructor(
    private val dao: MedicationHistoryDao
) : MedicationHistoryRepository {

    override suspend fun saveToHistory(medication: MedicationHistory) {
        dao.saveMedication(medication.toEntity())
    }

    override suspend fun getHistory(patientId: String): List<MedicationHistory> {
        return dao.getHistory(patientId).map { it.toDomain() }
    }

    override suspend fun deleteFromHistory(id: String) {
        dao.deleteFromHistory(id)
    }

    override suspend fun clearHistory(patientId: String) {
        dao.clearHistory(patientId)
    }

    private fun MedicationHistory.toEntity() = MedicationHistoryEntity(
        id           = id,
        patientId    = patientId,
        name         = name,
        dosage       = dosage,
        form         = form,
        instructions = instructions,
        notes        = notes,
        quantity     = quantity,
        price        = price,
        isActive     = isActive,
        startDate    = startDate,
        endDate      = endDate,
        photoPath    = photoPath,
        deletedAt    = deletedAt
    )

    private fun MedicationHistoryEntity.toDomain() = MedicationHistory(
        id           = id,
        patientId    = patientId,
        name         = name,
        dosage       = dosage,
        form         = form,
        instructions = instructions,
        notes        = notes,
        quantity     = quantity,
        price        = price,
        isActive     = isActive,
        startDate    = startDate,
        endDate      = endDate,
        photoPath    = photoPath,
        deletedAt    = deletedAt
    )
}