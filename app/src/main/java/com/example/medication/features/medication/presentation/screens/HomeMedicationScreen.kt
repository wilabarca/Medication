package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medication.features.medication.domain.model.Medication
import com.example.medication.features.medication.presentation.components.MedicationCard
import androidx.compose.foundation.lazy.items


@Composable
fun HomeMedicationScreen(){
    val medications = listOf(
        Medication("Paracetamol","12","9"),
        Medication("Ibuprofeno","13","2")
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick  = {}
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) {
        padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(medications){
                medication ->
                MedicationCard(medication)
            }
        }
    }
}