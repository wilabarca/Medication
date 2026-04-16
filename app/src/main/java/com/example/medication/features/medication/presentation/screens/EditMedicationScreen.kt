package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.components.EditMedicationForm
import com.example.medication.features.medication.presentation.components.MedicationUpdateSuccessDialog
import com.example.medication.features.medication.presentation.viewmodels.EditMedicationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedicationScreen(
    medication: Medication,
    onBack: () -> Unit = {},
    onUpdated: () -> Unit = {},
    viewModel: EditMedicationViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Diálogo de éxito: se muestra solo cuando el ViewModel confirma isSuccess ──
    if (state.isSuccess) {
        // Extrae el nombre del mensaje: ✅ Medicamento "Paracetamol" actualizado correctamente
        val medicationName = state.successMessage
            ?.substringAfter("\"")
            ?.substringBefore("\"")
            ?: medication.name

        MedicationUpdateSuccessDialog(
            medicationName = medicationName,
            onDismiss = {
                viewModel.resetState()   // limpia isSuccess en el ViewModel
                onUpdated()             // navega fuera de la pantalla
            }
        )
    }

    // ── Snackbar para errores ──────────────────────────────────────────────────
    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetState()
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
                    containerColor = Color(0xFFE65100),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                EditMedicationForm(
                    medication = medication,
                    onUpdate = { name, dosage, form, instructions, notes, quantity, price, isActive, photoPath ->
                        val quantityInt = quantity.toIntOrNull() ?: return@EditMedicationForm
                        val priceDouble = price.toDoubleOrNull()

                        viewModel.updateMedication(
                            id           = medication.id,
                            name         = name,
                            dosage       = dosage,
                            form         = form,
                            instructions = instructions,
                            notes        = notes,
                            quantity     = quantityInt,
                            price        = priceDouble,
                            isActive     = isActive,
                            photoPath    = photoPath
                        )
                        // NO llamamos onUpdated() aquí — esperamos a que isSuccess sea true
                    }
                )
            }
        }
    }
}