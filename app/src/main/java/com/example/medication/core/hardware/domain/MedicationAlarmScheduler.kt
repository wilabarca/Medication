package com.example.medication.core.hardware.domain

interface MedicationAlarmScheduler {
    fun scheduleAlarm(
        alarmId: Long,
        medicationName: String,
        hour: Int,
        minute: Int,
        selectedDays: List<Int>
    )
    fun cancelAlarm(alarmId: Long)
    fun canScheduleExact(): Boolean
}