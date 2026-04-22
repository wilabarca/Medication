package com.example.medication.features.caregiver.domain.usescases

import com.example.medication.features.caregiver.domain.models.CaregiverMenuItem
import javax.inject.Inject

class GetCaregiverHomeOptionsUseCase @Inject constructor() {

    operator fun invoke(): List<CaregiverMenuItem> {
        return listOf(
            CaregiverMenuItem(
                id = "patients",
                title = "Pacientes",
                description = "Ver y administrar pacientes"
            ),
            CaregiverMenuItem(
                id = "create_patient",
                title = "Agregar paciente",
                description = "Registrar un nuevo paciente"
            )
        )
    }
}