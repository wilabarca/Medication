package com.example.medication.features.home.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun HomeScreen(
    role: String,
    userId: String,
    linkedPatientId: String?,
    onOpenPatients: () -> Unit,
    onCreatePatient: () -> Unit,
    onOpenPatientLinking: () -> Unit,
    onOpenPatientMedications: (String) -> Unit,
    onOpenPatientAlarms: (String) -> Unit
) {
    when (role) {
        "caregiver" -> {
            CaregiverHomeScreen(
                onOpenPatients = onOpenPatients,
                onCreatePatient = onCreatePatient,
                onOpenPatientLinking = onOpenPatientLinking
            )
        }

        "patient" -> {
            PatientHomeScreen(
                linkedPatientId = linkedPatientId,
                onOpenMedications = onOpenPatientMedications,
                onOpenAlarms = onOpenPatientAlarms
            )
        }

        else -> {
            Text("Rol no válido o sesión incompleta")
        }
    }
}