package com.example.medication.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medication.core.database.entities.MedicationHistoryEntity

@Dao
interface MedicationHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMedication(medication: MedicationHistoryEntity)

    @Query("SELECT * FROM medication_history WHERE patientId = :patientId ORDER BY deletedAt DESC")
    suspend fun getHistory(patientId: String): List<MedicationHistoryEntity>

    @Query("DELETE FROM medication_history WHERE id = :id")
    suspend fun deleteFromHistory(id: String)

    @Query("DELETE FROM medication_history WHERE patientId = :patientId")
    suspend fun clearHistory(patientId: String)
}