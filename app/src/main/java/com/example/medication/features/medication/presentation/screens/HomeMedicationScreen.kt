package com.example.medication.features.medication.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.components.MedicationCard
import com.example.medication.features.medication.presentation.viewmodels.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMedicationScreen(
    onNavigateToSearch:  () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToEdit:    (Medication) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state             by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner    = LocalLifecycleOwner.current
    val scope             = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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

    // ── Modal de vinculación con token de 6 caracteres ────────────────────────
    if (showLinkDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isLinking) {
                    showLinkDialog = false
                    linkToken = ""
                }
            },
            shape          = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFEDE7F6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Link,
                            contentDescription = null,
                            tint     = Color(0xFF6A1B9A),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Text(
                        "Vincularme con cuidador",
                        fontWeight = FontWeight.Bold,
                        fontSize   = 16.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        "Ingresa el código que te compartió tu cuidador para ver tus medicamentos asignados.",
                        fontSize   = 13.sp,
                        color      = Color(0xFF607D8B),
                        lineHeight = 19.sp
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value         = linkToken,
                        onValueChange = { linkToken = it.uppercase().take(6) }, // ← máx 6 caracteres
                        label         = { Text("Código (ej. A1B2C3)") },
                        leadingIcon   = {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = null,
                                tint = Color(0xFF6A1B9A)
                            )
                        },
                        singleLine = true,
                        shape      = RoundedCornerShape(12.dp),
                        colors     = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6A1B9A),
                            focusedLabelColor  = Color(0xFF6A1B9A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "El código tiene 6 caracteres y lo genera tu cuidador.",
                        fontSize = 11.sp,
                        color    = Color(0xFF90A4AE)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (linkToken.length < 6) {
                            scope.launch {
                                snackbarHostState.showSnackbar("El código debe tener 6 caracteres")
                            }
                            return@Button
                        }
                        isLinking = true
                        viewModel.linkWithCaregiver(
                            token     = linkToken,
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
                    enabled = !isLinking && linkToken.length == 6, // ← habilitado con 6 chars
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape   = RoundedCornerShape(10.dp)
                ) {
                    if (isLinking) {
                        CircularProgressIndicator(
                            modifier    = Modifier.size(18.dp),
                            color       = Color.White,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text("Vincularme", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    if (!isLinking) {
                        showLinkDialog = false
                        linkToken = ""
                    }
                }) {
                    Text("Cancelar", color = Color(0xFF607D8B))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = "Mis medicamentos",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    AnimatedVisibility(
                        visible = !state.isLinked,
                        enter   = fadeIn() + scaleIn(),
                        exit    = fadeOut() + scaleOut()
                    ) {
                        IconButton(onClick = { showLinkDialog = true }) {
                            Icon(
                                Icons.Default.Link,
                                contentDescription = "Vincularme con cuidador",
                                tint = Color(0xFF6A1B9A)
                            )
                        }
                    }
                    AnimatedVisibility(
                        visible = state.isLinked,
                        enter   = fadeIn() + scaleIn(),
                        exit    = fadeOut() + scaleOut()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        Color(0xFF388E3C).copy(alpha = 0.12f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    "✓ Vinculado",
                                    fontSize   = 11.sp,
                                    color      = Color(0xFF388E3C),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            IconButton(onClick = { viewModel.unlinkCaregiver() }) {
                                Icon(
                                    Icons.Default.LinkOff,
                                    contentDescription = "Desvincularme",
                                    tint     = Color(0xFF9E9E9E),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "Historial")
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
                    Column(
                        modifier            = Modifier
                            .align(Alignment.Center)
                            .padding(padding)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text      = "⚠️ ${state.error}",
                            color     = Color(0xFFD32F2F),
                            textAlign = TextAlign.Center
                        )
                        TextButton(onClick = { viewModel.getMedications() }) {
                            Text("Reintentar")
                        }
                    }
                }

                !state.isLinked -> {
                    WelcomeUnlinkedContent(
                        modifier    = Modifier
                            .align(Alignment.Center)
                            .padding(padding),
                        onLinkClick = { showLinkDialog = true }
                    )
                }

                state.medications.isEmpty() -> {
                    Column(
                        modifier            = Modifier
                            .align(Alignment.Center)
                            .padding(padding)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("💊", fontSize = 48.sp)
                        Text(
                            "Tu cuidador aún no ha registrado medicamentos",
                            color     = Color(0xFF607D8B),
                            textAlign = TextAlign.Center,
                            fontSize  = 15.sp
                        )
                    }
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
                            MedicationCard(
                                medication       = medication,
                                isFavorite       = false,
                                onToggleFavorite = {},
                                onDelete         = {},
                                onEdit           = {},
                                readOnly         = true
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Pantalla de bienvenida sin vinculación ────────────────────────────────────
@Composable
private fun WelcomeUnlinkedContent(
    modifier: Modifier = Modifier,
    onLinkClick: () -> Unit
) {
    Column(
        modifier            = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    Brush.radialGradient(
                        listOf(Color(0xFFEDE7F6), Color(0xFFD1C4E9))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("💊", fontSize = 44.sp)
        }

        Text(
            text       = "Bienvenido a MedControl",
            fontWeight = FontWeight.Bold,
            fontSize   = 22.sp,
            color      = Color(0xFF1A1A2E),
            textAlign  = TextAlign.Center
        )

        Text(
            text       = "Aquí verás los medicamentos que tu cuidador te ha asignado. Para comenzar, ingresa el código de 6 caracteres que él te compartió.",
            fontSize   = 14.sp,
            color      = Color(0xFF607D8B),
            textAlign  = TextAlign.Center,
            lineHeight = 21.sp
        )

        Spacer(Modifier.height(4.dp))

        Button(
            onClick  = onLinkClick,
            shape    = RoundedCornerShape(14.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {
            Icon(
                Icons.Default.Link,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                "Ingresar código de vinculación",
                fontWeight = FontWeight.SemiBold,
                fontSize   = 15.sp
            )
        }

        Text(
            text      = "El código tiene 6 caracteres y lo genera tu cuidador desde su app.",
            fontSize  = 11.sp,
            color     = Color(0xFFB0BEC5),
            textAlign = TextAlign.Center
        )
    }
}