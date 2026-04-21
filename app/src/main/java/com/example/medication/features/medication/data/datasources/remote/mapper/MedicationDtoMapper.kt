package com.example.medication.features.medication.data.datasources.remote.mapper

import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.medication.domain.entities.Medication

fun MedicationDto.toDomain(): Medication {
    return Medication(
        id          = this.id ?: "",
        patientId   = this.patientId ?: "",   // ← userId → patientId
        name        = this.name ?: "",
        dosage      = this.dosage ?: "",
        form        = this.form ?: "",
        instructions = this.instructions,
        notes       = this.notes,
        quantity    = this.quantity ?: 0,
        price       = when (val p = this.price) {
            is Double -> p
            is String -> p.toDoubleOrNull()
            is Number -> p.toDouble()
            else      -> null
        },
        isActive    = this.isActive ?: true,
        startDate   = this.startDate,   // ← nuevo
        endDate     = this.endDate      // ← nuevo
    )
}

fun List<MedicationDto>.toDomainList(): List<Medication> = map { it.toDomain() }