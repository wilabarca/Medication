package com.example.medication.features.medication.data.datasources.local.alarm.models

data class MedicationAlarmLocalModel(
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
