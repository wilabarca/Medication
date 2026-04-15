package com.example.medication.features.medication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.database.dao.MedicationAlarmDao
import com.example.medication.core.database.entities.MedicationAlarmEntity
import com.example.medication.core.hardware.domain.MedicationAlarmScheduler
import com.example.medication.core.session.JwtSessionManager
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
    val error: String? = null,
    val alarmSaved: Boolean = false   // ← nuevo
)

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val dao: MedicationAlarmDao,
    private val alarmScheduler: MedicationAlarmScheduler,
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState(isLoading = true))
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    init {
        loadAlarmsForCurrentUser()
        syncCurrentUserAlarms()
    }

    private fun currentUserId(): String? = jwtSessionManager.getUserId()

    private fun loadAlarmsForCurrentUser() {
        val userId = currentUserId()

        if (userId.isNullOrBlank()) {
            _uiState.value = AlarmUiState(
                alarms = emptyList(),
                isLoading = false,
                error = "No se pudo obtener el usuario actual"
            )
            return
        }

        dao.getAlarmsByUserId(userId)
            .map { entities -> entities.map { it.toUiModel() } }
            .onEach { alarms ->
                _uiState.value = _uiState.value.copy(
                    alarms = alarms,
                    isLoading = false,
                    error = null
                )
            }
            .launchIn(viewModelScope)
    }

    fun syncCurrentUserAlarms() {
        viewModelScope.launch {
            val userId = currentUserId() ?: return@launch

            val allAlarms = dao.getAllAlarmsNow()
            allAlarms.forEach { alarmScheduler.cancelAlarm(it) }

            val currentUserAlarms = dao.getAlarmsByUserIdNow(userId)
            currentUserAlarms
                .filter { it.isEnabled }
                .forEach { alarmScheduler.scheduleAlarm(it) }
        }
    }

    fun saveAlarm(
        medicationId: String,
        medicationName: String,
        hour: Int,
        minute: Int,
        intervalHours: Int,
        selectedDays: List<Int>
    ) {
        viewModelScope.launch {
            val userId = currentUserId()

            if (userId.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo obtener el usuario actual"
                )
                return@launch
            }

            val entity = MedicationAlarmEntity(
                userId = userId,
                medicationId = medicationId,
                medicationName = medicationName,
                startDateMillis = System.currentTimeMillis(),
                endDateMillis = System.currentTimeMillis() + (30L * 24 * 60 * 60 * 1000),
                startHour = hour,
                startMinute = minute,
                intervalHours = intervalHours,
                selectedWeekDays = selectedDays,
                isEnabled = true
            )

            val alarmId = dao.insertAlarm(entity)
            alarmScheduler.scheduleAlarm(entity.copy(id = alarmId))
            _uiState.value = _uiState.value.copy(alarmSaved = true)  // ← nuevo
        }
    }

    fun resetAlarmSaved() {
        _uiState.value = _uiState.value.copy(alarmSaved = false)
    }

    fun updateAlarm(
        alarmId: Long,
        medicationId: String,
        medicationName: String,
        hour: Int,
        minute: Int,
        intervalHours: Int,
        selectedDays: List<Int>,
        isEnabled: Boolean
    ) {
        viewModelScope.launch {
            val userId = currentUserId()

            if (userId.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo obtener el usuario actual"
                )
                return@launch
            }

            val existing = dao.getAlarmByIdForUser(alarmId, userId)
            if (existing == null) {
                _uiState.value = _uiState.value.copy(
                    error = "No se encontró la alarma a editar"
                )
                return@launch
            }

            alarmScheduler.cancelAlarm(existing)

            val updated = existing.copy(
                medicationId = medicationId,
                medicationName = medicationName,
                startHour = hour,
                startMinute = minute,
                intervalHours = intervalHours,
                selectedWeekDays = selectedDays,
                isEnabled = isEnabled,
                updatedAt = System.currentTimeMillis()
            )

            dao.updateAlarm(updated)

            if (updated.isEnabled) {
                alarmScheduler.scheduleAlarm(updated)
            }
        }
    }

    fun deleteAlarm(alarmId: Long) {
        viewModelScope.launch {
            val userId = currentUserId()

            if (userId.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo obtener el usuario actual"
                )
                return@launch
            }

            val existing = dao.getAlarmByIdForUser(alarmId, userId)
            if (existing != null) {
                alarmScheduler.cancelAlarm(existing)
                dao.deleteAlarmById(alarmId)
            }
        }
    }

    fun setAlarmEnabled(alarmId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            val userId = currentUserId()

            if (userId.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo obtener el usuario actual"
                )
                return@launch
            }

            val existing = dao.getAlarmByIdForUser(alarmId, userId)
            if (existing == null) {
                _uiState.value = _uiState.value.copy(
                    error = "No se encontró la alarma"
                )
                return@launch
            }

            val updated = existing.copy(
                isEnabled = isEnabled,
                updatedAt = System.currentTimeMillis()
            )

            dao.updateAlarm(updated)

            if (isEnabled) {
                alarmScheduler.scheduleAlarm(updated)
            } else {
                alarmScheduler.cancelAlarm(existing)
            }
        }
    }

    private fun MedicationAlarmEntity.toUiModel() = AlarmUiModel(
        id = id,
        medicationId = medicationId,
        medicationName = medicationName,
        timeText = "%02d:%02d".format(startHour, startMinute),
        daysText = when {
            selectedWeekDays.isNotEmpty() -> selectedWeekDays
                .distinct()
                .sorted()
                .joinToString(" ") { index ->
                    listOf("D", "L", "M", "M", "J", "V", "S").getOrElse(index) { "" }
                }
            intervalHours > 0 -> "Cada $intervalHours h"
            else -> "Una vez"
        },
        isEnabled = isEnabled
    )
}