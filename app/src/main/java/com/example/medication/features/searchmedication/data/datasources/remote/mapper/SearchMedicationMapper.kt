package com.example.medication.features.searchmedication.data.datasources.remote.mapper

import com.example.medication.core.database.entities.SearchMedicineEntity
import com.example.medication.features.searchmedication.data.datasources.remote.models.MedicationDto
import com.example.medication.features.searchmedication.domain.entities.Medication

// Mapper: De Remoto (API) a Dominio
fun MedicationDto.toDomain() = Medication(
    id = id,
    name = name,
    description = description,
    quantity = quantity,
    price = price.toDoubleOrNull() ?: 0.0
)

// Mapper: De Remoto (API) a Local (DB)
fun MedicationDto.toEntity() = SearchMedicineEntity(
    id = id,
    name = name,
    description = description,
    quantity = quantity,
    price = price.toDoubleOrNull() ?: 0.0
)