package com.example.medication.features.searchmedication.data.datasources.local.mapper

import com.example.medication.core.database.entities.SearchMedicineEntity
import com.example.medication.features.searchmedication.domain.entities.Medication

// Mapper: De Local (DB) a Dominio
fun SearchMedicineEntity.toDomain() = Medication(
    id = id,
    name = name,
    description = description,
    quantity = quantity,
    price = price
)