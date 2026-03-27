package com.example.medication.features.medication.data.repositories

import com.example.medication.features.medication.data.datasources.remote.api.MedicationApi
import com.example.medication.features.medication.data.datasources.remote.mapper.toDomain
import com.example.medication.features.medication.data.datasources.remote.mapper.toDomainList
import com.example.medication.features.medication.data.datasources.remote.models.CreateMedicationRequest
import com.example.medication.features.medication.data.datasources.remote.models.UpdateMedicationRequest
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val api: MedicationApi
) : MedicationRepository {

    override suspend fun getMedications(): List<Medication> {
        return api.getMedications().toDomainList()
    }

    override suspend fun getMedicationById(id: String): Medication {
        return api.getMedicationById(id).toDomain()
    }

    override suspend fun createMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ) {
        val request = CreateMedicationRequest(
            name = name,
            description = description,
            quantity = quantity,
            price = price
        )
        api.createMedication(request)
    }

    override suspend fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double
    ): Medication {
        val request = UpdateMedicationRequest(
            name = name,
            description = description,
            quantity = quantity,
            price = price
        )
        return api.updateMedication(id, request).toDomain()
    }

    override suspend fun deleteMedication(id: String) {
        api.deleteMedication(id)
    }
}