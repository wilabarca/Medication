package com.example.medication.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medication.core.database.entities.MedicationAlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationAlarmDao {

    @Query("SELECT * FROM medications_alarms ORDER BY createdAt DESC")
    fun getAllAlarms(): Flow<List<MedicationAlarmEntity>>

    @Query("SELECT * FROM medications_alarms WHERE id = :alarmId LIMIT 1")
    suspend fun getAlarmById(alarmId: Long): MedicationAlarmEntity?

    @Query("SELECT * FROM medications_alarms WHERE medicationId = :medicationId ORDER BY createdAt DESC")
    fun getAlarmsByMedicationId(medicationId: String): Flow<List<MedicationAlarmEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAlarm(alarm: MedicationAlarmEntity): Long

    @Update
    suspend fun updateAlarm(alarm: MedicationAlarmEntity)

    @Query("DELETE FROM medications_alarms WHERE id = :alarmId")
    suspend fun deleteAlarmById(alarmId: Long)

    @Query("""
        UPDATE medications_alarms
        SET isEnabled = :isEnabled,
            updatedAt = :updatedAt
        WHERE id = :alarmId
    """)
    suspend fun updateAlarmStatus(
        alarmId: Long,
        isEnabled: Boolean,
        updatedAt: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM medications_alarms")
    suspend fun deleteAllAlarms()
}