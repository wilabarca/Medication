package com.example.medication.features.medication.data.repositories

import com.example.medication.core.database.dao.MedicationDao
import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.features.medication.data.datasources.remote.api.MedicationApi
import com.example.medication.features.medication.data.datasources.remote.mapper.toDomain
import com.example.medication.features.medication.data.datasources.remote.models.CreateMedicationRequest
import com.example.medication.features.medication.data.datasources.remote.models.UpdateMedicationRequest
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.domain.repositories.MedicationRepository
import javax.inject.Inject

class MedicationRepositoryImpl @Inject constructor(
    private val api: MedicationApi,
    private val dao: MedicationDao
) : MedicationRepository {

    override suspend fun getMedications(): List<Medication> {
        val remoteMedications = api.getMedications()

        remoteMedications.forEach { dto ->
            val id = dto.id ?: return@forEach
            val existing = dao.getById(id)
            dao.insertMedication(
                MedicationEntity(
                    id = id,
                    name = dto.name ?: "",
                    description = dto.description ?: "",
                    quantity = dto.quantity ?: 0,
                    price = when (val p = dto.price) {
                        is Double -> p
                        is String -> p.toDoubleOrNull() ?: 0.0
                        else -> 0.0
                    },
                    photoPath = existing?.photoPath
                )
            )
        }

        val remoteIds = remoteMedications.mapNotNull { it.id }.toSet()
        val localIds = dao.getAllMedications().map { it.id }.toSet()
        localIds.filter { it !in remoteIds }.forEach { id ->
            dao.deleteById(id)
        }

        return dao.getAllMedications().map { entity ->
            Medication(
                id = entity.id,
                name = entity.name,
                description = entity.description,
                quantity = entity.quantity,
                price = entity.price,
                photoPath = entity.photoPath
            )
        }
    }

    override suspend fun getMedicationById(id: String): Medication {
        val remote = api.getMedicationById(id).toDomain()
        val local = dao.getById(id)
        return remote.copy(photoPath = local?.photoPath)
    }

    // ← createMedication corregido
    override suspend fun createMedication(
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String?
    ) {
        val request = CreateMedicationRequest(name, description, quantity, price)
        api.createMedication(request)

        // La API no devuelve id — buscamos el recién creado por nombre
        val medications = api.getMedications()
        val newMedication = medications.lastOrNull { it.name?.trim() == name.trim() }
        android.util.Log.d("PHOTO_DEBUG", "newMedication encontrado: ${newMedication?.id}")

        newMedication?.id?.let { id ->
            dao.insertMedication(
                MedicationEntity(
                    id = id,
                    name = name,
                    description = description,
                    quantity = quantity,
                    price = price,
                    photoPath = photoPath
                )
            )
            android.util.Log.d("PHOTO_DEBUG", "createMedication insertó photoPath: $photoPath para id: $id")
        }
    }

    override suspend fun updateMedication(
        id: String,
        name: String,
        description: String,
        quantity: Int,
        price: Double,
        photoPath: String?
    ): Medication {
        val request = UpdateMedicationRequest(name, description, quantity, price)
        val remote = api.updateMedication(id, request).toDomain()
        dao.insertMedication(
            MedicationEntity(
                id = id,
                name = name,
                description = description,
                quantity = quantity,
                price = price,
                photoPath = photoPath
            )
        )
        android.util.Log.d("PHOTO_DEBUG", "updateMedication guardó photoPath: $photoPath para id: $id")
        return remote.copy(photoPath = photoPath)
    }

    override suspend fun deleteMedication(id: String) {
        api.deleteMedication(id)
        dao.deleteById(id)
        android.util.Log.d("PHOTO_DEBUG", "deleteMedication borró id: $id de Room")
    }
}