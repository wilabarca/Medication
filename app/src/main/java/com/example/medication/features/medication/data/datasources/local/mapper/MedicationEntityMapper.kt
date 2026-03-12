package com.example.medication.features.medication.data.datasources.local.mapper

import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.features.medication.domain.entities.Medication


// Mapper: De Local (DB) a Dominio
fun MedicationEntity.toDomain() = Medication(id, name, description, quantity, price)