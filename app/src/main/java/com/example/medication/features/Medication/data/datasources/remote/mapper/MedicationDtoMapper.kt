package com.example.medication.features.Medication.data.datasources.remote.mapper

import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.features.Medication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.Medication.domain.entities.Medication


// Mapper: De Remoto (DTO) a Dominio
fun MedicationDto.toDomain(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        price = this.price
    )
}

// Mapper: De Remoto (API) a Local (DB)
fun MedicationDto.toEntity() = MedicationEntity(id, name, description, quantity, price)