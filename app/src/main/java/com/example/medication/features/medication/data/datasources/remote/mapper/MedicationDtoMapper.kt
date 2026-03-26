package com.example.medication.features.medication.data.datasources.remote.mapper

import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.medication.domain.entities.Medication

fun MedicationDto.toDomain(): Medication {
    return Medication(
        id = this.id ?: "",
        name = this.name ?: "",
        description = this.description ?: "",
        quantity = this.quantity ?: 0,
        price = when (val p = this.price) {
            is Double -> p
            is String -> p.toDoubleOrNull() ?: 0.0
            is Number -> p.toDouble()
            else -> 0.0
        }
    )
}

fun List<MedicationDto>.toDomainList(): List<Medication> {
    return map { it.toDomain() }
}