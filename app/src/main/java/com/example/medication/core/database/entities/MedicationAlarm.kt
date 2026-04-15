package com.example.medication.core.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.medication.features.medication.data.datasources.local.converters.AlarmConverters

@Entity(
    tableName = "medications_alarms",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["medicationId"])
    ]
)
@TypeConverters(AlarmConverters::class)
data class MedicationAlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val medicationId: String,
    val medicationName: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val startHour: Int,
    val startMinute: Int,
    val intervalHours: Int = 0,
    val selectedWeekDays: List<Int> = emptyList(),
    val notifyBeforeMinutes: Int = 5,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)