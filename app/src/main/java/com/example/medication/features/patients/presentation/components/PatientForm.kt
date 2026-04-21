package com.example.medication.features.patients.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PatientForm(
    name: String,
    birthDate: String,
    relationship: String,
    notes: String,
    isActive: Boolean,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onBirthDateChange: (String) -> Unit,
    onRelationshipChange: (String) -> Unit,
    onNotesChange: (String) -> Unit,
    onIsActiveChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onCancel: () -> Unit,
    submitText: String = "Guardar paciente"
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = birthDate,
            onValueChange = onBirthDateChange,
            label = { Text("Fecha de nacimiento") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = relationship,
            onValueChange = onRelationshipChange,
            label = { Text("Relación") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notas") },
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Activo")
            Switch(
                checked = isActive,
                onCheckedChange = onIsActiveChange
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(submitText)
        }

        TextButton(
            onClick = onCancel,
            enabled = !isLoading
        ) {
            Text("Cancelar")
        }
    }
}