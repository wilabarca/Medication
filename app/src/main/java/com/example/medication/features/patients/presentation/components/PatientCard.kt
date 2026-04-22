package com.example.medication.features.patients.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medication.features.patients.domain.entities.Patient

@Composable
fun PatientCard(
    patient: Patient,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = patient.name,
                style = MaterialTheme.typography.titleMedium
            )

            patient.relationship?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = "Relación: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            patient.birthDate?.takeIf { it.isNotBlank() }?.let {
                Text(
                    text = "Nacimiento: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Text(
                text = if (patient.isActive) "Activo" else "Inactivo",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}