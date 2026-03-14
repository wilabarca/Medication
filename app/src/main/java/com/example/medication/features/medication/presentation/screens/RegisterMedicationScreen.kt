package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.medication.features.medication.presentation.components.RegisterMedicationForm
import com.example.medication.features.medication.presentation.viewmodels.RegisterMedicationViewModel

@Composable
fun RegisterMedicationScreen(
    viewModel: RegisterMedicationViewModel = hiltViewModel(),
    onMedicationRegistered: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

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

            state.isSuccess -> {
                onMedicationRegistered()
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

        if (state.error != null) {
            Text(
                text = state.error ?: "",
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}