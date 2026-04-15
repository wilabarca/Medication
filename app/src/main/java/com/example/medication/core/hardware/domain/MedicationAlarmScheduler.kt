package com.example.medication.core.hardware.domain

import com.example.medication.core.database.entities.MedicationAlarmEntity

interface MedicationAlarmScheduler {
    fun scheduleAlarm(alarm: MedicationAlarmEntity)
    fun cancelAlarm(alarm: MedicationAlarmEntity)
    fun canScheduleExact(): Boolean
}