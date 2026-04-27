package com.example.medication.features.patients.domain.repositories

import com.example.medication.features.patients.domain.entities.Patient

interface PatientRepository {

    suspend fun createPatient(
        caregiverUserId: String,
        linkedUserId: String?,
        name: String,
        birthDate: String?,
        relationship: String?,
        notes: String?,
        isActive: Boolean
    ): Patient

    suspend fun getPatientsByCaregiver(
        caregiverUserId: String
    ): List<Patient>

    suspend fun getPatientById(id: String): Patient

    suspend fun updatePatient(patient: Patient): Patient

    suspend fun deletePatient(id: String)

    suspend fun linkAccount(
        token: String,
        userId: String
    )
}