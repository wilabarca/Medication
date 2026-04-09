package com.example.medication.features.medication.presentation.viewmodels

data class AlarmUiModel(
    val id: Long = 0,
    val medicationName: String,
    val timeText: String,
    val daysText: String
)