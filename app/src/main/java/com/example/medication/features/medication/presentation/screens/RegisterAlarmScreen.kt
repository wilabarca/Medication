package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.presentation.viewmodels.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterAlarmScreen(
    onBack: () -> Unit = {},
    onAlarmSaved: () -> Unit = {},
    viewModel: AlarmViewModel = hiltViewModel()  // ✅ mismo ViewModel
) {
    var medicationName by remember { mutableStateOf("") }
    var startHour by remember { mutableStateOf("08") }
    var startMinute by remember { mutableStateOf("00") }
    var intervalHours by remember { mutableStateOf("8") }
    var selectedDays by remember { mutableStateOf(setOf<Int>()) }

    val days = listOf("D", "L", "M", "M", "J", "V", "S")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Alarma", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text("Medicamento", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = medicationName,
                onValueChange = { medicationName = it },
                label = { Text("Nombre del medicamento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Hora de inicio", style = MaterialTheme.typography.labelLarge)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = startHour,
                    onValueChange = { if (it.length <= 2) startHour = it },
                    label = { Text("HH") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Text(":", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = startMinute,
                    onValueChange = { if (it.length <= 2) startMinute = it },
                    label = { Text("MM") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Text("Repetir cada (horas)", style = MaterialTheme.typography.labelLarge)
            OutlinedTextField(
                value = intervalHours,
                onValueChange = { intervalHours = it },
                label = { Text("Intervalo en horas") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text("Días de la semana", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEachIndexed { index, day ->
                    val isSelected = selectedDays.contains(index)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedDays = if (isSelected) {
                                selectedDays - index
                            } else {
                                selectedDays + index
                            }
                        },
                        label = { Text(day, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // ✅ guarda en Room
                    viewModel.saveAlarm(
                        medicationName = medicationName,
                        hour = startHour.toIntOrNull() ?: 8,
                        minute = startMinute.toIntOrNull() ?: 0,
                        intervalHours = intervalHours.toIntOrNull() ?: 8,
                        selectedDays = selectedDays.toList()
                    )
                    onAlarmSaved()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = medicationName.isNotBlank() && selectedDays.isNotEmpty()
            ) {
                Text("Guardar Alarma")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}