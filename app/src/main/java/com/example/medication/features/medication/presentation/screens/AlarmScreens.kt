package com.example.medication.features.medication.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.medication.features.medication.presentation.components.AlarmCard
import com.example.medication.features.medication.presentation.viewmodels.AlarmUiModel
import com.example.medication.features.medication.presentation.viewmodels.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreens(
    onBack: () -> Unit = {},
    onAddAlarm: () -> Unit = {},
    onCalendarClick: (AlarmUiModel) -> Unit = {},
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Alarmas", fontWeight = FontWeight.Bold) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAlarm,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar alarma",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.alarms.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "No hay alarmas registradas",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "Pulsa + para agregar una",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(
                            items = state.alarms,
                            key = { it.id }
                        ) { alarm ->
                            SwipeToDeleteAlarmCard(
                                alarm = alarm,
                                onDelete = { viewModel.deleteAlarm(alarm.id) },
                                onCalendarClick = onCalendarClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteAlarmCard(
    alarm: AlarmUiModel,
    onDelete: () -> Unit,
    onCalendarClick: (AlarmUiModel) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                showConfirmDialog = true
            }
            false  // ← no elimina automáticamente, espera confirmación
        }
    )

    // Diálogo de confirmación
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Eliminar alarma") },
            text = { Text("¿Eliminar la alarma de ${alarm.medicationName}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onDelete()
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,  // solo deslizar de derecha a izquierda
        enableDismissFromEndToStart = true,
        backgroundContent = {
            // Fondo rojo con icono de basura
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.Transparent
                },
                label = "swipe_color"
            )
            val scale by animateFloatAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> 1f
                    else -> 0.75f
                },
                label = "swipe_scale"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White,
                    modifier = Modifier
                        .scale(scale)
                        .padding(end = 16.dp)
                )
            }
        }
    ) {
        AlarmCard(
            alarm = alarm,
            onCalendarClick = onCalendarClick
        )
    }
}