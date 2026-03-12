package com.example.medication.features.medication.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.Modifier


@Composable
fun RegisterMedicationForm(
    onRegister: (String, String, String, String) -> Unit
)
{

    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("")  }
    var price by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("")}

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Registro Medicamentos",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Nombre")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = quantity,
            onValueChange = {quantity = it},
            label = {Text("Cantidad")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = {price = it},
            label = {Text("Precio")},
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = {description = it},
            label = {Text("Descipción")},
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Button(
            onClick = {
                onRegister(name, quantity, price, description)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Medicamento")
        }
    }

}