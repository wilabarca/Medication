package com.example.medication.features.patients.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyPatientsState(
    onCreatePatient: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "No tienes pacientes aún",
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onCreatePatient) {
            Text("Agregar paciente")
        }
    }
}