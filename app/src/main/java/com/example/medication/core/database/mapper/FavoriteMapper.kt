package com.example.medication.core.database.mapper

import com.example.medication.core.database.entities.FavoriteEntity
import com.example.medication.features.medication.domain.entities.Medication

fun FavoriteEntity.toMedication(): Medication {
    return Medication(
        id = this.id,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        price = this.price
    )
}

fun Medication.toFavoriteEntity(): FavoriteEntity {
    return FavoriteEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        quantity = this.quantity,
        price = this.price
    )
}

fun List<FavoriteEntity>.toMedicationList(): List<Medication> = map { it.toMedication() }