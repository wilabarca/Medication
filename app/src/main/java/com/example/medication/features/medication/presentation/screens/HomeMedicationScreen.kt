package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.favorites.presentation.viewmodels.FavoritesViewModel
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.components.MedicationCard
import com.example.medication.features.medication.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMedicationScreen(
    onNavigateToRegister: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    onNavigateToFavorites: () -> Unit = {},
    onNavigateToAlarm: () -> Unit = {},
    onNavigateToEdit: (Medication) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    val state             by viewModel.uiState.collectAsStateWithLifecycle()
    val favoritesMap      by favoritesViewModel.favoritesMap.collectAsStateWithLifecycle()
    val lifecycleOwner    = LocalLifecycleOwner.current
    val scope             = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // ── Estado del modal ───────────────────────────────────────────
    var showLinkDialog by remember { mutableStateOf(false) }
    var linkToken      by remember { mutableStateOf("") }
    var isLinking      by remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.getMedications()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // ── Modal de vinculación ───────────────────────────────────────
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLinking) {
                    showLinkDialog = false
                    linkToken = ""
                }
            },
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Link,
                        contentDescription = null,
                        tint = Color(0xFF6A1B9A)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Vincularme con cuidador",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        "Ingresa el código que te compartió tu cuidador:",
                        fontSize = 13.sp,
                        color = Color(0xFF607D8B)
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value         = linkToken,
                        onValueChange = { linkToken = it.uppercase().take(8) },
                        label         = { Text("Código (ej. CF1CAC2A)") },
                        leadingIcon   = {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint = Color(0xFF6A1B9A)
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            focusedLabelColor  = Color(0xFF6A1B9A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "El código tiene 8 caracteres y lo genera tu cuidador.",
                        fontSize = 11.sp,
                        color = Color(0xFF90A4AE)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (linkToken.length < 8) {
                            scope.launch {
                                snackbarHostState.showSnackbar("El código debe tener 8 caracteres")
                            }
                            return@Button
                        }
                        isLinking = true
                        viewModel.linkWithCaregiver(
                            token = linkToken,
                            onSuccess = {
                                isLinking      = false
                                showLinkDialog = false
                                linkToken      = ""
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message  = "✅ Vinculación exitosa con tu cuidador",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            onError = { errorMsg ->
                                isLinking = false
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message  = "❌ $errorMsg",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    },
                    enabled = !isLinking,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape  = RoundedCornerShape(10.dp)
                ) {
                    if (isLinking) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(18.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Vincularme")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        if (!isLinking) {
                            showLinkDialog = false
                            linkToken = ""
                        }
                    }
                ) {
                    Text("Cancelar", color = Color(0xFF607D8B))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Mis Medicamentos") },
                actions = {
                    // ── Icono de vinculación ───────────────────────
                    IconButton(onClick = { showLinkDialog = true }) {
                        Icon(
                            Icons.Default.Link,
                            contentDescription = "Vincularme con cuidador",
                            tint = Color(0xFF6A1B9A)
                        )
                    }
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(Icons.Default.Star, contentDescription = "Favoritos")
                    }
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FloatingActionButton(
                    onClick        = onNavigateToAlarm,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Alarm, contentDescription = "Alarma")
                }
                FloatingActionButton(
                    onClick        = onNavigateToRegister,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor   = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFF0F7FF), Color(0xFFFFFFFF))
                    )
                )
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(padding)
                    )
                }

                state.error != null -> {
                    Text(
                        text     = state.error ?: "Error",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(padding)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.medications,
                            key   = { it.id }
                        ) { medication ->
                            LaunchedEffect(medication.id) {
                                favoritesViewModel.checkIsFavorite(medication.id)
                            }
                            MedicationCard(
                                medication       = medication,
                                isFavorite       = favoritesMap[medication.id] ?: false,
                                onToggleFavorite = {
                                    favoritesViewModel.toggleFavorite(medication)
                                },
                                onDelete = { id -> viewModel.deleteMedication(id) },
                                onEdit   = { med -> onNavigateToEdit(med) }
                            )
                        }
                    }
                }
            }
        }
    }
}