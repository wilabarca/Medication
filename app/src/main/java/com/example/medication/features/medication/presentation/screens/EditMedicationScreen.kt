package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.components.EditMedicationForm
import com.example.medication.features.medication.presentation.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationScreen(
    medication: Medication,
    onBack: () -> Unit = {},
    onUpdated: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.error) {
        if (state.error == null && !state.isLoading) {
            // no hacer nada aquí
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Medicamento", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                EditMedicationForm(
                    medication = medication,
                    onUpdate = { name, quantity, price, description, photoPath ->
                        val quantityInt = quantity.toIntOrNull() ?: return@EditMedicationForm
                        val priceDouble = price.toDoubleOrNull() ?: return@EditMedicationForm
                        viewModel.updateMedication(
                            id = medication.id,
                            name = name,
                            description = description,
                            quantity = quantityInt,
                            price = priceDouble,
                            photoPath = photoPath
                        )
                        onUpdated()
                    }
                )
            }
        }
    }
}