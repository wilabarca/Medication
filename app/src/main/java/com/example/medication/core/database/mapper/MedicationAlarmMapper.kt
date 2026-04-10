
package com.example.medication.core.database.mapper

import com.example.medication.core.database.entities.MedicationAlarmEntity
import com.example.medication.features.medication.data.datasources.local.alarm.models.MedicationAlarmLocalModel

fun MedicationAlarmEntity.toLocalModel(): MedicationAlarmLocalModel {
    return MedicationAlarmLocalModel(
        id = id,
        medicationId = medicationId,
        medicationName = medicationName,
        startDateMillis = startDateMillis,
        endDateMillis = endDateMillis,
        startHour = startHour,
        startMinute = startMinute,
        intervalHours = intervalHours,
        selectedWeekDays = selectedWeekDays,
        notifyBeforeMinutes = notifyBeforeMinutes,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MedicationAlarmLocalModel.toEntity(): MedicationAlarmEntity {
    return MedicationAlarmEntity(
        id = id,
        medicationId = medicationId,
        medicationName = medicationName,
        startDateMillis = startDateMillis,
        endDateMillis = endDateMillis,
        startHour = startHour,
        startMinute = startMinute,
        intervalHours = intervalHours,
        selectedWeekDays = selectedWeekDays,
        notifyBeforeMinutes = notifyBeforeMinutes,
        isEnabled = isEnabled,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}