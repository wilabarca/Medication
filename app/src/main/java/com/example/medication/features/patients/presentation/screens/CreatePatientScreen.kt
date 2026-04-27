package com.example.medication.features.patients.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Notes
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.StickyNote2
import androidx.compose.material.icons.rounded.ToggleOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.patients.presentation.viewmodels.PatientsViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

private fun formatBirthDateInput(value: String): String {
    val digits = value.filter { it.isDigit() }.take(8)

    return buildString {
        append(digits.take(4))

        if (digits.length > 4) {
            append("-")
            append(digits.substring(4, minOf(6, digits.length)))
        }

        if (digits.length > 6) {
            append("-")
            append(digits.substring(6, digits.length))
        }
    }
}

private fun normalizeBirthDateOrNull(value: String): String? {
    val trimmed = value.trim()

    if (trimmed.isBlank()) return null

    val dateRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")
    if (!dateRegex.matches(trimmed)) return null

    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        formatter.isLenient = false

        val parsedDate = formatter.parse(trimmed) ?: return null
        val formattedDate = formatter.format(parsedDate)

        if (formattedDate == trimmed) trimmed else null
    } catch (error: Exception) {
        null
    }
}

// ── Paleta crear paciente ──────────────────────────────────────────────────────
private val CpTeal = Color(0xFF00695C)
private val CpTealLight = Color(0xFFE0F2F1)
private val CpTealDark = Color(0xFF004D40)
private val CpBlue = Color(0xFF1565C0)
private val CpGreen = Color(0xFF2E7D32)
private val CpGreenLight = Color(0xFFE8F5E9)
private val CpRed = Color(0xFFC62828)
private val CpCard = Color(0xFFFFFFFF)
private val CpBg = Color(0xFFF4F6F9)
private val CpText = Color(0xFF0D1F2D)
private val CpTextSec = Color(0xFF546E7A)
private val CpDivider = Color(0xFFECEFF1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePatientScreen(
    caregiverUserId: String,
    onBack: () -> Unit = {},
    onCreated: () -> Unit = {},
    viewModel: PatientsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var name by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var relationship by rememberSaveable { mutableStateOf("") }
    var notes by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            viewModel.clearMessages()
            onCreated()
        }
    }

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
                        text = "Nuevo Paciente",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CpTeal
                )
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(CpTealDark, CpTeal, CpBlue)
                            ),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    color = Color.White.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PersonAdd,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Registrar Paciente",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Ingresa los datos del paciente",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                CpCard(
                    title = "Datos Personales",
                    icon = Icons.Rounded.Person
                ) {
                    CpField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Nombre completo *",
                        icon = Icons.Rounded.Badge
                    )

                    CpField(
                        value = birthDate,
                        onValueChange = { value ->
                            birthDate = formatBirthDateInput(value)
                        },
                        label = "Fecha de nacimiento (YYYY-MM-DD)",
                        icon = Icons.Rounded.CalendarMonth,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    CpField(
                        value = relationship,
                        onValueChange = { relationship = it },
                        label = "Relación (ej. Hijo, Madre, Paciente)",
                        icon = Icons.Rounded.FamilyRestroom
                    )
                }

                CpCard(
                    title = "Notas adicionales",
                    icon = Icons.Rounded.Notes
                ) {
                    CpField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = "Notas (alergias, condiciones, etc.)",
                        icon = Icons.Rounded.StickyNote2,
                        minLines = 3
                    )
                }

                CpCard(
                    title = "Estado",
                    icon = Icons.Rounded.ToggleOn
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (isActive) CpGreenLight else Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isActive) {
                                        Icons.Filled.CheckCircle
                                    } else {
                                        Icons.Filled.Cancel
                                    },
                                    contentDescription = null,
                                    tint = if (isActive) CpGreen else CpRed,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(Modifier.width(8.dp))

                                Column {
                                    Text(
                                        text = "Estado del paciente",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 13.sp,
                                        color = CpText
                                    )

                                    Text(
                                        text = if (isActive) "Activo" else "Inactivo",
                                        fontSize = 11.sp,
                                        color = CpTextSec
                                    )
                                }
                            }

                            Switch(
                                checked = isActive,
                                onCheckedChange = { isActive = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CpGreen,
                                    checkedTrackColor = Color(0xFFA5D6A7),
                                    uncheckedThumbColor = CpRed,
                                    uncheckedTrackColor = Color(0xFFEF9A9A)
                                )
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        val normalizedBirthDate = normalizeBirthDateOrNull(birthDate)

                        if (birthDate.isNotBlank() && normalizedBirthDate == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "La fecha debe tener formato YYYY-MM-DD. Ejemplo: 1998-05-21"
                                )
                            }
                        } else {
                            viewModel.createPatient(
                                caregiverUserId = caregiverUserId,
                                name = name.trim(),
                                birthDate = normalizedBirthDate,
                                relationship = relationship.trim().ifBlank { null },
                                notes = notes.trim().ifBlank { null },
                                isActive = isActive
                            )
                        }
                    },
                    enabled = name.trim().isNotBlank() && !uiState.isLoading,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CpTeal
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .shadow(6.dp, RoundedCornerShape(14.dp))
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            text = "Guardar Paciente",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CpCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CpCard),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(CpTealLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = CpTeal,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(Modifier.width(8.dp))

                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = CpTeal,
                    letterSpacing = 0.5.sp
                )

                Spacer(Modifier.width(8.dp))

                HorizontalDivider(
                    color = CpDivider,
                    modifier = Modifier.weight(1f)
                )
            }

            content()
        }
    }
}

@Composable
private fun CpField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                fontSize = 13.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CpTeal,
                modifier = Modifier.size(20.dp)
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CpTeal,
            unfocusedBorderColor = Color(0xFFB2DFDB),
            focusedLabelColor = CpTeal,
            unfocusedContainerColor = CpCard,
            focusedContainerColor = CpCard
        ),
        modifier = modifier.fillMaxWidth(),
        minLines = minLines,
        keyboardOptions = keyboardOptions
    )
}