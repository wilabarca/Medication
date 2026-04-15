package com.example.medication.features.favorites.data.datasources.local.mapper

import com.example.medication.core.database.entities.FavoriteEntity
import com.example.medication.features.medication.domain.entities.Medication

fun FavoriteEntity.toDomain() = Medication(
    id = id,
    userId = "",                  // favoritos son locales
    name = name,
    dosage = description,         // reuso temporal del campo viejo
    form = "No especificado",     // porque FavoriteEntity viejo no lo tiene
    instructions = null,
    notes = null,
    quantity = quantity,
    price = price,
    isActive = true,
    photoPath = null
)

fun Medication.toEntity() = FavoriteEntity(
    id = id,
    name = name,
    description = dosage,         // reuso temporal del campo viejo
    quantity = quantity,
    price = price ?: 0.0
)