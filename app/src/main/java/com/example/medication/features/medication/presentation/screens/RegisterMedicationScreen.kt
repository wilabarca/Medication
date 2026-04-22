package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.presentation.components.MedicationSuccessDialog
import com.example.medication.features.medication.presentation.components.RegisterMedicationForm
import com.example.medication.features.medication.presentation.viewmodels.RegisterMedicationViewModel

@Composable
fun RegisterMedicationScreen(
    viewModel: RegisterMedicationViewModel = hiltViewModel(),
    onMedicationRegistered: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    if (state.isSuccess) {
        val medicationName = state.successMessage
            ?.substringAfter("\"")
            ?.substringBefore("\"")
            ?: "Medicamento"

        MedicationSuccessDialog(
            medicationName = medicationName,
            onDismiss = {
                viewModel.resetState()
                onMedicationRegistered()
            }
        )
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                RegisterMedicationForm(
                    onRegister = {
                            name,
                            dosage,
                            form,
                            instructions,
                            notes,
                            quantity,
                            price,
                            isActive,
                            photoPath,
                            startDate,
                            endDate ->

                        val quantityInt = quantity.toIntOrNull()
                        val priceDouble = price.toDoubleOrNull()

                        when {
                            name.isBlank() -> {
                                // nombre vacío — no guarda
                            }
                            dosage.isBlank() -> {
                                // dosis vacía — no guarda
                            }
                            form.isBlank() -> {
                                // forma vacía — no guarda
                            }
                            quantityInt == null -> {
                                // cantidad inválida — no guarda
                            }
                            else -> viewModel.registerMedication(
                                name         = name,
                                dosage       = dosage,
                                form         = form,
                                instructions = instructions.ifBlank { null },
                                notes        = notes.ifBlank { null },
                                quantity     = quantityInt,
                                price        = priceDouble,
                                isActive     = isActive,
                                startDate    = startDate,
                                endDate      = endDate,
                                photoPath    = photoPath
                            )
                        }
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier  = Modifier.align(Alignment.BottomCenter)
        )
    }
}