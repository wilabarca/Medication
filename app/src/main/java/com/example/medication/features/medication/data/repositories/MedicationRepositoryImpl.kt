package com.example.medication.features.medication.data.repositories


import com.example.medication.features.medication.data.datasources.remote.api.MedicationApi
import com.example.medication.features.medication.data.datasources.remote.mapper.toDomain
import com.example.medication.features.medication.data.datasources.remote.models.CreateMedicationRequest
import com.example.medication.features.medication.data.datasources.remote.models.UpdateMedicationRequest
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject


class MedicationRepositoryImpl @Inject constructor(
private val api: MedicationApi
) : MedicationRepository {

    override suspend fun getMedications(): List<Medication> {
        val response = api.getMedications()
        return response.map { it.toDomain() }
    }

    override suspend fun getMedicationById(id: String): Medication {
        val response = api.getMedicationById(id)
        return response.toDomain()
    }

    override suspend fun createMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ): Medication {
        val response = api.createMedication(
            CreateMedicationRequest(name, description, quantity, price)
        )
        return response.toDomain()
    }

    override suspend fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ): Medication {
        val response = api.updateMedication(
            id,
            UpdateMedicationRequest(name, description, quantity, price)
        )
        return response.toDomain()
    }

    override suspend fun deleteMedication(id: String) {
        api.deleteMedication(id)
    }
}