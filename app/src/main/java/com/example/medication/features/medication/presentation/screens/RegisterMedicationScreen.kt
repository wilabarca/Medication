package com.example.medication.features.medication.presentation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.medication.features.medication.presentation.components.RegisterMedicationForm

@Composable
fun RegisterMedicationScreen(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ){

        RegisterMedicationForm(
            onRegister = {
                name, quantity, price, description ->
                println(name)
                println(quantity)
                println(price)
                println(description)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterMedicationPreview(){
    RegisterMedicationScreen()
}