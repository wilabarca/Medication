package com.example.medication.features.medication.presentation.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.medication.features.medication.presentation.components.AlarmCard
import com.example.medication.features.medication.presentation.viewmodels.AlarmViewModel


@Composable
fun AlarmScreens(
    alarms: List<AlarmViewModel> = sampleAlarmList(),
    onAddAlarm: () -> Unit = {},
    onCalendarClick: (AlarmViewModel) -> Unit = {}
){
    Scaffold(
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
    ){padding ->
        if (alarms.isEmpty()){
            Text(
                text = "No hay alarmas registradas",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            )
        }
        else{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(alarms){ alarm ->
                    AlarmCard(
                        alarm = alarm,
                        onCalendarClick = onCalendarClick
                    )
                }
            }
        }
    }
}

private fun sampleAlarmList(): List<AlarmViewModel>{
    return listOf(
        AlarmViewModel(
            id ="1",
            medicationName = "Paracetamol",
            timeText = "12:00",
            daysText = "D L M M J V S"
        ),
        AlarmViewModel(
            id = "2",
            medicationName = "Paracetamol",
            timeText = "12:00",
            daysText = "D L MM J V S"
        )
    )
}


