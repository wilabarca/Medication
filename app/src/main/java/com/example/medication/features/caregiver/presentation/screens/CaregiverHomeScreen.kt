package com.example.medication.features.caregiver.presentation.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.caregiver.presentation.viewmodels.CaregiverHomeViewModel
import com.example.medication.features.patients.domain.entities.Patient

// ── Paleta cuidador ────────────────────────────────────────────────────────────
private val CgTeal       = Color(0xFF00695C)
private val CgTealLight  = Color(0xFFE0F2F1)
private val CgTealDark   = Color(0xFF004D40)
private val CgBlue       = Color(0xFF1565C0)
private val CgBlueLight  = Color(0xFFE3F2FD)
private val CgGreen      = Color(0xFF2E7D32)
private val CgGreenLight = Color(0xFFE8F5E9)
private val CgAmber      = Color(0xFFE65100)
private val CgAmberLight = Color(0xFFFFF3E0)
private val CgCard       = Color(0xFFFFFFFF)
private val CgBg         = Color(0xFFF4F6F9)
private val CgText       = Color(0xFF0D1F2D)
private val CgTextSec    = Color(0xFF546E7A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverHomeScreen(
    onNavigateToCreatePatient: () -> Unit = {},
    onNavigateToPatientDetail: (Patient) -> Unit = {},
    viewModel: CaregiverHomeViewModel = hiltViewModel()
) {
    val uiState       by viewModel.uiState.collectAsStateWithLifecycle()
    var showTokenDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Mostrar snackbar si hay mensaje
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    // Diálogo del token de vinculación
    if (showTokenDialog) {
        LinkTokenDialog(
            token       = uiState.linkToken,
            isLoading   = uiState.isGeneratingToken,
            onGenerate  = { viewModel.generateLinkToken() },
            onDismiss   = { showTokenDialog = false; viewModel.clearToken() }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Panel del Cuidador",
                            fontWeight = FontWeight.Bold,
                            fontSize   = 18.sp,
                            color      = Color.White
                        )
                        Text(
                            "${uiState.patients.size} paciente(s) vinculado(s)",
                            fontSize = 12.sp,
                            color    = Color.White.copy(alpha = 0.80f)
                        )
                    }
                },
                actions = {
                    // Botón generar token de vinculación
                    IconButton(onClick = { showTokenDialog = true }) {
                        Icon(
                            Icons.Rounded.QrCode,
                            contentDescription = "Generar token de vinculación",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CgTeal
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick        = onNavigateToCreatePatient,
                containerColor = CgTeal,
                contentColor   = Color.White
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Agregar paciente")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CgBg)
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color    = CgTeal
                    )
                }

                uiState.patients.isEmpty() -> {
                    EmptyCaregiverState(
                        onAddPatient  = onNavigateToCreatePatient,
                        onLinkPatient = { showTokenDialog = true },
                        modifier      = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier            = Modifier.fillMaxSize(),
                        contentPadding      = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Banner resumen
                        item {
                            CaregiverSummaryBanner(
                                patientCount    = uiState.patients.size,
                                onGenerateToken = { showTokenDialog = true }
                            )
                        }

                        items(uiState.patients, key = { it.id }) { patient ->
                            PatientMedicationCard(
                                patient   = patient,
                                onClick   = { onNavigateToPatientDetail(patient) }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Banner resumen ─────────────────────────────────────────────────────────────
@Composable
private fun CaregiverSummaryBanner(
    patientCount: Int,
    onGenerateToken: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.horizontalGradient(listOf(CgTealDark, CgTeal, CgBlue)),
                RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment    = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier             = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    "Mis Pacientes",
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 18.sp
                )
                Text(
                    "Supervisando $patientCount paciente(s)",
                    color    = Color.White.copy(alpha = 0.80f),
                    fontSize = 13.sp
                )
            }
            OutlinedButton(
                onClick = onGenerateToken,
                colors  = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border  = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                shape   = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Rounded.QrCode, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Vincular", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ── Card de paciente con medicamentos ─────────────────────────────────────────
@Composable
private fun PatientMedicationCard(
    patient: Patient,
    onClick: () -> Unit
) {
    Card(
        onClick    = onClick,
        modifier   = Modifier.fillMaxWidth(),
        shape      = RoundedCornerShape(16.dp),
        colors     = CardDefaults.cardColors(containerColor = CgCard),
        elevation  = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── Cabecera paciente ──────────────────────────────────────────────
            Row(
                verticalAlignment    = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Avatar inicial
                Box(
                    modifier         = Modifier
                        .size(48.dp)
                        .background(
                            Brush.radialGradient(listOf(CgTeal, CgBlue)),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = patient.name.take(1).uppercase(),
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        patient.name,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 15.sp,
                        color      = CgText
                    )
                    patient.relationship?.let {
                        Text(it, fontSize = 12.sp, color = CgTextSec)
                    }
                }

                // Badge activo
                if (patient.isActive) {
                    Box(
                        modifier = Modifier
                            .background(CgGreenLight, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "Activo",
                            fontSize   = 11.sp,
                            color      = CgGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Icon(
                    Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = CgTextSec
                )
            }

            // ── Medicamentos del paciente ──────────────────────────────────────
            if (patient.medications.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFECEFF1))
                Spacer(Modifier.height(10.dp))

                Text(
                    "Medicamentos activos",
                    fontSize   = 11.sp,
                    color      = CgTextSec,
                    fontWeight = FontWeight.Medium
                )
                Spacer(Modifier.height(6.dp))

                patient.medications.take(3).forEach { med ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(CgTeal, CircleShape)
                        )
                        Text(med.name, fontSize = 13.sp, color = CgText, modifier = Modifier.weight(1f))
                        Text(
                            med.dosage,
                            fontSize = 11.sp,
                            color    = CgTextSec,
                            modifier = Modifier
                                .background(CgTealLight, RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                if (patient.medications.size > 3) {
                    Text(
                        "+${patient.medications.size - 3} más",
                        fontSize = 11.sp,
                        color    = CgTeal,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Rounded.Medication,
                        null,
                        tint     = CgTextSec,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        "Sin medicamentos registrados",
                        fontSize = 12.sp,
                        color    = CgTextSec
                    )
                }
            }
        }
    }
}

// ── Diálogo de token de vinculación ───────────────────────────────────────────
@Composable
private fun LinkTokenDialog(
    token: String?,
    isLoading: Boolean,
    onGenerate: () -> Unit,
    onDismiss: () -> Unit
) {
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape  = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = CgCard),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier            = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Ícono
                Box(
                    modifier         = Modifier
                        .size(72.dp)
                        .background(CgTealLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Rounded.QrCode,
                        null,
                        tint     = CgTeal,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Text(
                    "Token de Vinculación",
                    fontWeight  = FontWeight.Bold,
                    fontSize    = 18.sp,
                    color       = CgText,
                    textAlign   = TextAlign.Center
                )

                Text(
                    "Comparte este código con tu paciente para que pueda vincularse a tu cuenta.",
                    fontSize  = 13.sp,
                    color     = CgTextSec,
                    textAlign = TextAlign.Center
                )

                // Área del token
                if (token != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CgTealLight, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                token,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 22.sp,
                                color      = CgTealDark,
                                textAlign  = TextAlign.Center,
                                letterSpacing = 4.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Válido por 15 minutos",
                                fontSize = 11.sp,
                                color    = CgTextSec
                            )
                        }
                    }

                    // Botón copiar
                    OutlinedButton(
                        onClick = {
                            clipboard.setText(AnnotatedString(token))
                            copied = true
                        },
                        shape  = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CgTeal),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, CgTeal),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            if (copied) Icons.Default.CheckCircle else Icons.Rounded.ContentCopy,
                            null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (copied) "¡Copiado!" else "Copiar código",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Botón generar
                Button(
                    onClick  = onGenerate,
                    enabled  = !isLoading,
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = CgTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color       = Color.White,
                            modifier    = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Rounded.Refresh, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (token == null) "Generar token" else "Regenerar token",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                TextButton(onClick = onDismiss) {
                    Text("Cerrar", color = CgTextSec)
                }
            }
        }
    }
}

// ── Estado vacío ───────────────────────────────────────────────────────────────
@Composable
private fun EmptyCaregiverState(
    onAddPatient: () -> Unit,
    onLinkPatient: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier            = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier         = Modifier
                .size(90.dp)
                .background(CgTealLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Rounded.Groups,
                null,
                tint     = CgTeal,
                modifier = Modifier.size(48.dp)
            )
        }
        Text(
            "Sin pacientes aún",
            fontWeight = FontWeight.Bold,
            fontSize   = 18.sp,
            color      = CgText
        )
        Text(
            "Agrega un paciente manualmente o genera un token para que un paciente se vincule a tu cuenta.",
            fontSize  = 13.sp,
            color     = CgTextSec,
            textAlign = TextAlign.Center
        )
        Button(
            onClick  = onAddPatient,
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = CgTeal),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PersonAdd, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Agregar paciente", fontWeight = FontWeight.SemiBold)
        }
        OutlinedButton(
            onClick  = onLinkPatient,
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = CgTeal),
            border   = androidx.compose.foundation.BorderStroke(1.5.dp, CgTeal),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Rounded.QrCode, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Generar token de vinculación", fontWeight = FontWeight.Medium)
        }
    }
}