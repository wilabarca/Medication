package com.example.medication.features.medication.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.medication.features.medication.domain.entities.Medication


@Composable
fun MedicationCard(
    medication: Medication
){

    Card(
       modifier = Modifier
           .fillMaxWidth()
           .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF4DA3D9))
                .padding(16.dp)
        ) {
            Text(
                text ="$ ${medication.price}",
                color = Color.White
            )

            Text(
                text = medication.quantity.toString(),
                color = Color.White
            )
        }
    }
}