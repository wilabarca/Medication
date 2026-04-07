package com.example.medication.features.favorites.data.datasources.local.mapper

import com.example.medication.core.database.entities.FavoriteEntity
import com.example.medication.features.medication.domain.entities.Medication

// Mapper: De Local (DB) a Dominio
fun FavoriteEntity.toDomain() = Medication(
    id = id,
    name = name,
    description = description,
    quantity = quantity,
    price = price
)

// Mapper: De Dominio a Local (DB)
fun Medication.toEntity() = FavoriteEntity(
    id = id,
    name = name,
    description = description,
    quantity = quantity,
    price = price
)