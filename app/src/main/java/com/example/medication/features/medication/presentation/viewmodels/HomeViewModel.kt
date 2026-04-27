package com.example.medication.features.medication.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medication.core.hardware.domain.DeviceIdProvider
import com.example.medication.core.session.JwtSessionManager
import com.example.medication.features.history.domain.entities.MedicationHistory
import com.example.medication.features.history.domain.usecases.SaveToHistoryUseCase
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.usecases.DeleteMedicationUseCase
import com.example.medication.features.medication.domain.usecases.GetMedicationsUseCase
import com.example.medication.features.medication.domain.usecases.UpdateMedicationUseCase
import com.example.medication.features.patients.domain.usecases.LinkWithCaregiverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

data class HomeUiState(
    val medications: List<Medication> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLinked: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val deleteMedicationUseCase: DeleteMedicationUseCase,
    private val updateMedicationUseCase: UpdateMedicationUseCase,
    private val linkWithCaregiverUseCase: LinkWithCaregiverUseCase,
    private val saveToHistoryUseCase: SaveToHistoryUseCase,
    private val jwtSessionManager: JwtSessionManager,
    private val deviceIdProvider: DeviceIdProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        checkIfLinked()
    }

    // ── Verifica si ya está vinculado al iniciar ───────────────────────────────
    private fun checkIfLinked() {
        viewModelScope.launch {
            val linkedId = jwtSessionManager.getLinkedPatientId()
            val alreadyLinked = !linkedId.isNullOrBlank()
            Log.d(TAG, "checkIfLinked → linkedId=$linkedId, alreadyLinked=$alreadyLinked")
            _uiState.value = _uiState.value.copy(isLinked = alreadyLinked)
            if (alreadyLinked) getMedications()
        }
    }

    // ── Obtener medicamentos ───────────────────────────────────────────────────
    fun getMedications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val patientId = jwtSessionManager.getLinkedPatientId()
                Log.d(TAG, "getMedications → patientId=$patientId")

                if (patientId.isNullOrBlank()) {
                    Log.w(TAG, "getMedications → sin patientId, abortando")
                    _uiState.value = _uiState.value.copy(
                        medications = emptyList(),
                        isLoading   = false,
                        error       = null
                    )
                    return@launch
                }

                val medications = getMedicationsUseCase(patientId)
                Log.d(TAG, "getMedications → total=${medications.size}")
                _uiState.value = _uiState.value.copy(
                    medications = medications,
                    isLoading   = false,
                    error       = null
                )
            } catch (e: Exception) {
                Log.e(TAG, "getMedications → error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error     = e.message ?: "Error al obtener medicamentos"
                )
            }
        }
    }

    // ── Eliminar + guardar en historial ────────────────────────────────────────
    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "deleteMedication → id=${medication.id}")
                saveToHistoryUseCase(
                    MedicationHistory(
                        id           = medication.id,
                        patientId    = medication.patientId,
                        name         = medication.name,
                        dosage       = medication.dosage,
                        form         = medication.form,
                        instructions = medication.instructions,
                        notes        = medication.notes,
                        quantity     = medication.quantity,
                        price        = medication.price,
                        isActive     = medication.isActive,
                        startDate    = medication.startDate,
                        endDate      = medication.endDate,
                        photoPath    = medication.photoPath,
                        deletedAt    = System.currentTimeMillis()
                    )
                )
                deleteMedicationUseCase(medication.id)
                getMedications()
            } catch (e: Exception) {
                Log.e(TAG, "deleteMedication → error", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al eliminar medicamento"
                )
            }
        }
    }

    // ── Actualizar medicamento ─────────────────────────────────────────────────
    fun updateMedication(
        id: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        startDate: String? = null,
        endDate: String?   = null,
        photoPath: String? = null
    ) {
        viewModelScope.launch {
            try {
                val linkedPatientId = jwtSessionManager.getLinkedPatientId()
                Log.d(TAG, "updateMedication → id=$id, linkedPatientId=$linkedPatientId")

                if (linkedPatientId.isNullOrBlank()) {
                    Log.w(TAG, "updateMedication → sin patientId vinculado")
                    _uiState.value = _uiState.value.copy(error = "No hay paciente vinculado")
                    return@launch
                }
                val deviceId = deviceIdProvider.getDeviceId()
                updateMedicationUseCase(
                    id           = id,
                    patientId    = linkedPatientId,
                    name         = name,
                    dosage       = dosage,
                    form         = form,
                    instructions = instructions,
                    notes        = notes,
                    quantity     = quantity,
                    price        = price,
                    isActive     = isActive,
                    startDate    = startDate,
                    endDate      = endDate,
                    photoPath    = photoPath,
                    deviceId     = deviceId
                )
                getMedications()
            } catch (e: Exception) {
                Log.e(TAG, "updateMedication → error", e)
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Error al actualizar medicamento"
                )
            }
        }
    }

    // ── Vincular con cuidador mediante token ───────────────────────────────────
    // El backend guarda el deviceId como linkedUserId del paciente.
    // Causas comunes de error 400:
    //   1. Token ya usado (isActive = false en BD)
    //   2. Token expirado (> 30 min)
    //   3. Paciente ya vinculado a otro deviceId distinto
    // En todos los casos el repositorio ahora extrae el mensaje real del servidor.
    fun linkWithCaregiver(
        token: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val deviceId = deviceIdProvider.getDeviceId()
                Log.d(TAG, "linkWithCaregiver → token=$token, deviceId=$deviceId")

                val patientId = linkWithCaregiverUseCase(
                    token  = token,
                    userId = deviceId
                )
                Log.d(TAG, "linkWithCaregiver → patientId recibido=$patientId")

                jwtSessionManager.saveLinkedPatientId(patientId)
                Log.d(TAG, "linkWithCaregiver → patientId guardado correctamente")

                _uiState.value = _uiState.value.copy(isLinked = true)

                getMedications()

                onSuccess()
            } catch (e: Exception) {
                // Ahora el mensaje viene directo del body del servidor (ej: "Token inválido o inactivo")
                Log.e(TAG, "linkWithCaregiver → error al vincular: ${e.message}", e)
                onError(e.message ?: "Error al vincular con el cuidador")
            }
        }
    }

    // ── Desvincular ───────────────────────────────────────────────────────────
    fun unlinkCaregiver() {
        viewModelScope.launch {
            Log.d(TAG, "unlinkCaregiver → desvinculando")
            jwtSessionManager.saveLinkedPatientId("")
            _uiState.value = HomeUiState()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}