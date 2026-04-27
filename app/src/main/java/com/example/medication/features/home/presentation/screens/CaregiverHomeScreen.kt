package com.example.medication.features.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CaregiverHomeScreen(
    onOpenPatients: () -> Unit,
    onCreatePatient: () -> Unit,
    onOpenPatientLinking: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Inicio cuidador",
            style = MaterialTheme.typography.headlineSmall
        )

        Button(
            onClick = onOpenPatients,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver pacientes")
        }

        OutlinedButton(
            onClick = onCreatePatient,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear paciente")
        }

        OutlinedButton(
            onClick = onOpenPatientLinking,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Rounded.Link,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Vincular paciente")
        }
    }
}