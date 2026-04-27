package com.example.medication.features.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PatientHomeScreen(
    linkedPatientId: String?,
    onOpenMedications: (String) -> Unit,
    onOpenAlarms: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Inicio paciente",
            style = MaterialTheme.typography.headlineSmall
        )

        if (linkedPatientId.isNullOrBlank()) {
            Text(
                text = "Tu cuenta aún no está vinculada a un paciente."
            )
            return@Column
        }

        Text(
            text = "Consulta tu tratamiento y configura tus recordatorios.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = { onOpenMedications(linkedPatientId) }
        ) {
            Text("Ver mis medicamentos")
        }

        Button(
            onClick = { onOpenAlarms(linkedPatientId) }
        ) {
            Text("Mis alarmas")
        }
    }
}