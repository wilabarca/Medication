package com.example.medication.features.caregiver.domain.repositories

interface CaregiverRepository {
    /**
     * Genera un token de vinculación de corta duración (15 min)
     * que el paciente usará para vincularse al cuidador.
     */
    suspend fun generateLinkToken(caregiverId: String): String
}