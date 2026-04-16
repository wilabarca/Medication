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

    // ── Diálogo de éxito: se muestra solo cuando el ViewModel confirma isSuccess ──
    if (state.isSuccess) {
        // Extraemos el nombre del medicamento del mensaje del ViewModel
        // Formato del mensaje: ✅ Medicamento "Paracetamol" registrado correctamente
        val medicationName = state.successMessage
            ?.substringAfter("\"")
            ?.substringBefore("\"")
            ?: "Medicamento"

        MedicationSuccessDialog(
            medicationName = medicationName,
            onDismiss = {
                viewModel.resetState()       // limpia isSuccess en el ViewModel
                onMedicationRegistered()     // navega fuera de la pantalla
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

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                RegisterMedicationForm(
                    onRegister = { name, dosage, form, instructions, notes, quantity, price, isActive, photoPath ->
                        // Validaciones antes de llamar al ViewModel
                        val quantityInt = quantity.toIntOrNull()
                        val priceDouble = price.toDoubleOrNull()

                        when {
                            name.isBlank()      -> return@RegisterMedicationForm
                            dosage.isBlank()    -> return@RegisterMedicationForm
                            form.isBlank()      -> return@RegisterMedicationForm
                            quantityInt == null -> return@RegisterMedicationForm
                            else -> viewModel.registerMedication(
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