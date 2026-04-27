package com.example.medication.features.history.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.medication.features.history.domain.entities.MedicationHistory
import com.example.medication.features.history.presentation.viewmodels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de medicamentos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = Color(0xFF0277BD),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF4F6F9))
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color    = Color(0xFF0277BD)
                    )
                }

                uiState.history.isEmpty() -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            tint     = Color(0xFF90A4AE),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "No hay medicamentos eliminados",
                            color    = Color(0xFF607D8B),
                            fontSize = 15.sp
                        )
                        Text(
                            "Aquí aparecerán los que elimines",
                            color    = Color(0xFF90A4AE),
                            fontSize = 12.sp
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(
                            items = uiState.history,
                            key   = { it.id }
                        ) { item ->
                            HistoryCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryCard(item: MedicationHistory) {
    val deletedDate = remember(item.deletedAt) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(Date(item.deletedAt))
    }

    Card(
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFFFEBEE), RoundedCornerShape(12.dp))
            ) {
                Icon(
                    Icons.Default.Medication,
                    contentDescription = null,
                    tint     = Color(0xFFD32F2F),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                    color      = Color(0xFF1C2B3A)
                )
                Text(
                    "${item.dosage} · ${item.form}",
                    fontSize = 12.sp,
                    color    = Color(0xFF607D8B)
                )
                item.startDate?.let {
                    Text("Inicio: $it", fontSize = 11.sp, color = Color(0xFF90A4AE))
                }
                item.endDate?.let {
                    Text("Término: $it", fontSize = 11.sp, color = Color(0xFF90A4AE))
                }
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = null,
                        tint     = Color(0xFFD32F2F),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "Eliminado: $deletedDate",
                        fontSize = 11.sp,
                        color    = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}