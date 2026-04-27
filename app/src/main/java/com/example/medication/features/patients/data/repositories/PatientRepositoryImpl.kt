package com.example.medication.features.patients.data.repositories

import com.example.medication.features.patients.data.dataresources.remote.api.PatientsApi
import com.example.medication.features.patients.data.dataresources.remote.mapper.toDomain
import com.example.medication.features.patients.data.dataresources.remote.models.CreatePatientRequest
import com.example.medication.features.patients.data.dataresources.remote.models.LinkAccountRequest
import com.example.medication.features.patients.data.dataresources.remote.models.PatientDto
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.domain.repositories.PatientRepository
import javax.inject.Inject

class PatientRepositoryImpl @Inject constructor(
    private val api: PatientsApi
) : PatientRepository {

    override suspend fun createPatient(
        caregiverUserId: String,
        linkedUserId: String?,
        name: String,
        birthDate: String?,
        relationship: String?,
        notes: String?,
        isActive: Boolean
    ): Patient {
        val response = api.createPatient(
            CreatePatientRequest(
                caregiverUserId = caregiverUserId,
                linkedUserId    = linkedUserId,
                name            = name,
                birthDate       = birthDate,
                relationship    = relationship,
                notes           = notes,
                isActive        = isActive
            )
        )
        return response.toDomain()
    }

    override suspend fun getPatientsByCaregiver(
        caregiverUserId: String
    ): List<Patient> {
        return api.getPatientsByCaregiver(caregiverUserId).map { it.toDomain() }
    }

    override suspend fun getPatientById(id: String): Patient {
        return api.getPatientById(id).toDomain()
    }

    override suspend fun updatePatient(patient: Patient): Patient {
        val response = api.updatePatient(
            id = patient.id,
            patient = PatientDto(
                id              = patient.id,
                caregiverUserId = patient.caregiverUserId,
                linkedUserId    = patient.linkedUserId,
                name            = patient.name,
                birthDate       = patient.birthDate,
                relationship    = patient.relationship,
                notes           = patient.notes,
                isActive        = patient.isActive
            )
        )
        return response.toDomain()
    }

    override suspend fun deletePatient(id: String) {
        api.deletePatient(id)
    }

    override suspend fun linkAccount(
        token: String,
        userId: String
    ) {
        api.linkAccount(
            LinkAccountRequest(
                token  = token,
                userId = userId
            )
        )
    }
}