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
    startDate: String? = null,  // ← nuevo
    endDate: String? = null,    // ← nuevo
    photoPath: String? = null
) {
    viewModelScope.launch {
        try {
            val currentPatientId = jwtSessionManager.getUserId()
            if (currentPatientId.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    error = "No se pudo obtener el usuario actual desde el token"
                )
                return@launch
            }
            val deviceId = deviceIdProvider.getDeviceId()
            updateMedicationUseCase(
                id           = id,
                patientId    = currentPatientId,
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
            _uiState.value = _uiState.value.copy(
                error = e.message ?: "Error al actualizar medicamento"
            )
        }
    }
}