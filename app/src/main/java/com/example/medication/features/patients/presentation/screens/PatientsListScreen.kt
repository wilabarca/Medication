package com.example.medication.features.patients.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.presentation.components.EmptyPatientsState
import com.example.medication.features.patients.presentation.components.PatientCard
import com.example.medication.features.patients.presentation.viewmodels.PatientsViewModel

@Composable
fun PatientsListScreen(
    caregiverUserId: String,
    onCreatePatient: () -> Unit,
    onPatientSelected: (Patient) -> Unit,
    viewModel: PatientsViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedPatientForLink by remember { mutableStateOf<Patient?>(null) }
    var showLinkDialog by remember { mutableStateOf(false) }

    LaunchedEffect(caregiverUserId) {
        viewModel.loadPatients(caregiverUserId)
    }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }

        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreatePatient
            ) {
                Text("+")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.patients.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        EmptyPatientsState(
                            onCreatePatient = onCreatePatient
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.patients) { patient ->
                            PatientCard(
                                patient = patient,
                                onClick = {
                                    onPatientSelected(patient)
                                },
                                onGenerateLinkToken = {
                                    selectedPatientForLink = patient
                                    showLinkDialog = true
                                    viewModel.clearLinkToken()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showLinkDialog && selectedPatientForLink != null) {
        AlertDialog(
            onDismissRequest = {
                showLinkDialog = false
                selectedPatientForLink = null
                viewModel.clearLinkToken()
            },
            title = {
                Text("Generar código de vinculación")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Paciente: ${selectedPatientForLink?.name.orEmpty()}"
                    )

                    if (uiState.linkToken == null) {
                        Text(
                            text = "Genera un código para que el paciente pueda vincular su cuenta."
                        )
                    } else {
                        Text("Código de vinculación:")

                        Text(
                            text = uiState.linkToken.orEmpty(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Expira en: ${uiState.linkTokenExpiresAt.orEmpty()}"
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        selectedPatientForLink?.let { patient ->
                            viewModel.generateLinkToken(
                                patientId = patient.id,
                                caregiverUserId = caregiverUserId
                            )
                        }
                    },
                    enabled = !uiState.isGeneratingToken
                ) {
                    if (uiState.isGeneratingToken) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Generar")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLinkDialog = false
                        selectedPatientForLink = null
                        viewModel.clearLinkToken()
                    }
                ) {
                    Text("Cerrar")
                }
            }
        )
    }
}