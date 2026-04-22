package com.example.medication.features.caregiver.data.repositories

import com.example.medication.features.caregiver.domain.repositories.CaregiverRepository
import javax.inject.Inject

class CaregiverRepositoryImpl @Inject constructor(
    // TODO: inyecta tu API cuando el backend exponga el endpoint de token
    // private val caregiverApi: CaregiverApi
) : CaregiverRepository {

    override suspend fun generateLinkToken(caregiverId: String): String {
        // TODO: reemplaza por llamada real a la API
        // return caregiverApi.generateLinkToken(caregiverId).token
        return generateLocalToken(caregiverId)
    }

    // Token local temporal mientras el backend no tiene el endpoint
    private fun generateLocalToken(caregiverId: String): String {
        val chars   = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
        val random  = java.security.SecureRandom()
        return (1..6).map { chars[random.nextInt(chars.length)] }.joinToString("")
    }
}