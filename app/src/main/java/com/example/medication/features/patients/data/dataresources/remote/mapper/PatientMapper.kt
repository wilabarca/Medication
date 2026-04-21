package com.example.medication.features.patients.data.dataresources.remote.mapper

import com.example.medication.features.patients.data.dataresources.remote.models.PatientDto
import com.example.medication.features.patients.domain.entities.Patient

fun PatientDto.toDomain(): Patient {
    return Patient(
        id = id,
        caregiverUserId = caregiverUserId,
        linkedUserId = linkedUserId,
        name = name,
        birthDate = birthDate,
        relationship = relationship,
        notes = notes,
        isActive = isActive
    )
}