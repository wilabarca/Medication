package com.example.medication.features.patients.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.patients.presentation.viewmodels.PatientsViewModel

// ── Paleta crear paciente ──────────────────────────────────────────────────────
private val CpTeal       = Color(0xFF00695C)
private val CpTealLight  = Color(0xFFE0F2F1)
private val CpTealDark   = Color(0xFF004D40)
private val CpBlue       = Color(0xFF1565C0)
private val CpBlueLight  = Color(0xFFE3F2FD)
private val CpGreen      = Color(0xFF2E7D32)
private val CpGreenLight = Color(0xFFE8F5E9)
private val CpRed        = Color(0xFFC62828)
private val CpCard       = Color(0xFFFFFFFF)
private val CpBg         = Color(0xFFF4F6F9)
private val CpText       = Color(0xFF0D1F2D)
private val CpTextSec    = Color(0xFF546E7A)
private val CpDivider    = Color(0xFFECEFF1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePatientScreen(
    onBack: () -> Unit = {},
    onCreated: () -> Unit = {},
    viewModel: PatientsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Campos del formulario
    var name         by rememberSaveable { mutableStateOf("") }
    var birthDate    by rememberSaveable { mutableStateOf("") }
    var relationship by rememberSaveable { mutableStateOf("") }
    var notes        by rememberSaveable { mutableStateOf("") }
    var isActive     by rememberSaveable { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }

    // Navegar al crear exitosamente
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            onCreated()
        }
    }

    // Mostrar error en snackbar
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nuevo Paciente",
                        fontWeight = FontWeight.Bold,
                        color      = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CpTeal)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CpBg)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Header ──────────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(CpTealDark, CpTeal, CpBlue)),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.PersonAdd,
                                null,
                                tint     = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                "Registrar Paciente",
                                color      = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 18.sp
                            )
                            Text(
                                "Ingresa los datos del paciente",
                                color    = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // ── Datos personales ─────────────────────────────────────────
                CpCard(title = "Datos Personales", icon = Icons.Rounded.Person) {
                    CpField(
                        value         = name,
                        onValueChange = { name = it },
                        label         = "Nombre completo *",
                        icon          = Icons.Rounded.Badge
                    )
                    CpField(
                        value         = birthDate,
                        onValueChange = { birthDate = it },
                        label         = "Fecha de nacimiento (YYYY-MM-DD)",
                        icon          = Icons.Rounded.CalendarMonth
                    )
                    CpField(
                        value         = relationship,
                        onValueChange = { relationship = it },
                        label         = "Relación (ej. Hijo, Madre, Paciente)",
                        icon          = Icons.Rounded.FamilyRestroom
                    )
                }

                // ── Notas ────────────────────────────────────────────────────
                CpCard(title = "Notas adicionales", icon = Icons.Rounded.Notes) {
                    CpField(
                        value         = notes,
                        onValueChange = { notes = it },
                        label         = "Notas (alergias, condiciones, etc.)",
                        icon          = Icons.Rounded.StickyNote2,
                        minLines      = 3
                    )
                }

                // ── Estado ───────────────────────────────────────────────────
                CpCard(title = "Estado", icon = Icons.Rounded.ToggleOn) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                if (isActive) CpGreenLight else Color(0xFFFFEBEE),
                                RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment    = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier             = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                    null,
                                    tint     = if (isActive) CpGreen else CpRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(
                                        "Estado del paciente",
                                        fontWeight = FontWeight.Medium,
                                        fontSize   = 13.sp,
                                        color      = CpText
                                    )
                                    Text(
                                        if (isActive) "Activo" else "Inactivo",
                                        fontSize = 11.sp,
                                        color    = CpTextSec
                                    )
                                }
                            }
                            Switch(
                                checked         = isActive,
                                onCheckedChange = { isActive = it },
                                colors          = SwitchDefaults.colors(
                                    checkedThumbColor   = CpGreen,
                                    checkedTrackColor   = Color(0xFFA5D6A7),
                                    uncheckedThumbColor = CpRed,
                                    uncheckedTrackColor = Color(0xFFEF9A9A)
                                )
                            )
                        }
                    }
                }

                // ── Botón guardar ────────────────────────────────────────────
                Button(
                    onClick = {
                        viewModel.createPatient(
                            name         = name.trim(),
                            birthDate    = birthDate.trim().ifBlank { null },
                            relationship = relationship.trim().ifBlank { null },
                            notes        = notes.trim().ifBlank { null },
                            isActive     = isActive
                        )
                    },
                    enabled  = name.isNotBlank() && !uiState.isLoading,
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = CpTeal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color       = Color.White,
                            modifier    = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Save, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(
                            "Guardar Paciente",
                            fontWeight = FontWeight.Bold,
                            fontSize   = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ── Card de sección ────────────────────────────────────────────────────────────
@Composable
private fun CpCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CpCard),
        modifier  = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier            = Modifier.padding(16.dp)
        ) {
            // Encabezado de sección
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(30.dp)
                        .background(CpTealLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = CpTeal, modifier = Modifier.size(16.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    title,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 13.sp,
                    color         = CpTeal,
                    letterSpacing = 0.5.sp
                )
                Spacer(Modifier.width(8.dp))
                HorizontalDivider(color = CpDivider, modifier = Modifier.weight(1f))
            }
            content()
        }
    }
}

// ── Campo de texto ─────────────────────────────────────────────────────────────
@Composable
private fun CpField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label         = { Text(label, fontSize = 13.sp) },
        leadingIcon   = {
            Icon(icon, null, tint = CpTeal, modifier = Modifier.size(20.dp))
        },
        shape    = RoundedCornerShape(12.dp),
        colors   = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = CpTeal,
            unfocusedBorderColor    = Color(0xFFB2DFDB),
            focusedLabelColor       = CpTeal,
            unfocusedContainerColor = CpCard,
            focusedContainerColor   = CpCard
        ),
        modifier = modifier.fillMaxWidth(),
        minLines = minLines
    )
}