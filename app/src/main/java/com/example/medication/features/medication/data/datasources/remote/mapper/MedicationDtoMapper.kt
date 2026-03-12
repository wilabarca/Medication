package com.example.medication.features.medication.data.datasources.remote.mapper

import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.medication.domain.entities.Medication

fun MedicationDto.toDomain(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        price = this.price
    )
}

