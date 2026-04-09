package com.example.medication.features.medication.data.datasources.local.alarm.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "medications_alarms",
    indices = [
        Index(value = ["MedicationId"])
    ]
    )
data class MedicationAlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: String ,
    val medicationId: String,
    val medicationName: String,
    val startDateMillis: Long,
    val endDateMillis: Long,
    val startHour: Int,
    val startMinute: Int,
    val intervalHours: Int,
    val selectedWeekDays: List<Int>,
    val notifyBeforeMinutes: Int = 5,
    val isEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)