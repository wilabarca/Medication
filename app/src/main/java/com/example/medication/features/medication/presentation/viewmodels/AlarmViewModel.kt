package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.features.medication.data.datasources.local.alarm.dao.MedicationAlarmDao
import com.example.medication.features.medication.data.datasources.local.alarm.entities.MedicationAlarmEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmUiState(
    val alarms: List<AlarmUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val dao: MedicationAlarmDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    init {
        loadAlarms()
    }

    private fun loadAlarms() {
        dao.getAllAlarms()
            .map { entities -> entities.map { it.toUiModel() } }
            .onEach { alarms ->
                _uiState.value = _uiState.value.copy(
                    alarms = alarms,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope)
    }

    fun saveAlarm(
        medicationName: String,
        hour: Int,
        minute: Int,
        intervalHours: Int,
        selectedDays: List<Int>
    ) {
        viewModelScope.launch {
            val entity = MedicationAlarmEntity(
                medicationId = "",
                medicationName = medicationName,
                startDateMillis = System.currentTimeMillis(),
                endDateMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                startHour = hour,
                startMinute = minute,
                intervalHours = intervalHours,
                selectedWeekDays = selectedDays
            )
            dao.insertAlarm(entity)
        }
    }

    fun deleteAlarm(id: Long) {
        viewModelScope.launch {
            dao.deleteAlarmById(id)
        }
    }

    private fun MedicationAlarmEntity.toUiModel() = AlarmUiModel(
        id = id,
        medicationName = medicationName,
        timeText = "%02d:%02d".format(startHour, startMinute),
        daysText = selectedWeekDays.joinToString(" ") { index ->
            listOf("D", "L", "M", "M", "J", "V", "S").getOrElse(index) { "" }
        }
    )
}