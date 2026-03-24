package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.presentation.components.RegisterMedicationForm
import com.example.medication.features.medication.presentation.viewmodels.RegisterMedicationViewModel

@Composable
fun RegisterMedicationScreen(
    viewModel: RegisterMedicationViewModel = hiltViewModel(),
    onMedicationRegistered: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            state.successMessage?.let {
                snackbarHostState.showSnackbar(it)
            }
            viewModel.resetState()
            onMedicationRegistered()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetState()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            else -> {
                RegisterMedicationForm(
                    onRegister = { name, quantity, price, description ->
                        val quantityInt = quantity.toIntOrNull()
                        val priceDouble = price.toDoubleOrNull()

                        when {
                            name.isBlank() -> return@RegisterMedicationForm
                            description.isBlank() -> return@RegisterMedicationForm
                            quantityInt == null -> return@RegisterMedicationForm
                            priceDouble == null -> return@RegisterMedicationForm
                            else -> {
                                viewModel.registerMedication(
                                    name = name,
                                    description = description,
                                    quantity = quantityInt,
                                    price = priceDouble
                                )
                            }
                        }
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}