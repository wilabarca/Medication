package com.example.medication.features.medication.data.repositories

import android.util.Log
import com.example.medication.core.database.dao.MedicationDao
import com.example.medication.core.database.entities.MedicationEntity
import com.example.medication.features.medication.data.datasources.remote.api.MedicationApi
import com.example.medication.features.medication.data.datasources.remote.mapper.toDomain
import com.example.medication.features.medication.data.datasources.remote.models.CreateMedicationRequest
import com.example.medication.features.medication.data.datasources.remote.models.MedicationDto
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
                dto.toEntity(
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
            entity.toDomain()
        }
    }

    override suspend fun getMedicationById(id: String): Medication {
        val remote = api.getMedicationById(id).toDomain()
        val local = dao.getById(id)

        return remote.copy(photoPath = local?.photoPath)
    }

    override suspend fun createMedication(
        userId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        photoPath: String?
    ) {
        val request = CreateMedicationRequest(
            userId = userId,
            name = name,
            dosage = dosage,
            form = form,
            instructions = instructions,
            notes = notes,
            quantity = quantity,
            price = price,
            isActive = isActive
        )

        val created = api.createMedication(request).data

        dao.insertMedication(
            created.toEntity(photoPath = photoPath)
        )

        Log.d(
            "PHOTO_DEBUG",
            "createMedication insertó photoPath: $photoPath para id: ${created.id}"
        )
    }

    override suspend fun updateMedication(
        id: String,
        userId: String,
        name: String,
        dosage: String,
        form: String,
        instructions: String?,
        notes: String?,
        quantity: Int,
        price: Double?,
        isActive: Boolean,
        photoPath: String?
    ): Medication {
        val request = UpdateMedicationRequest(
            userId = userId,
            name = name,
            dosage = dosage,
            form = form,
            instructions = instructions,
            notes = notes,
            quantity = quantity,
            price = price,
            isActive = isActive
        )

        val remote = api.updateMedication(id, request).data.toDomain()

        dao.insertMedication(
            MedicationEntity(
                id = remote.id,
                userId = remote.userId,
                name = remote.name,
                dosage = remote.dosage,
                form = remote.form,
                instructions = remote.instructions,
                notes = remote.notes,
                quantity = remote.quantity,
                price = remote.price,
                isActive = remote.isActive,
                photoPath = photoPath
            )
        )

        Log.d(
            "PHOTO_DEBUG",
            "updateMedication guardó photoPath: $photoPath para id: $id"
        )

        return remote.copy(photoPath = photoPath)
    }

    override suspend fun deleteMedication(id: String) {
        api.deleteMedication(id)
        dao.deleteById(id)
        Log.d("PHOTO_DEBUG", "deleteMedication borró id: $id de Room")
    }

    private fun MedicationDto.toEntity(photoPath: String?): MedicationEntity {
        return MedicationEntity(
            id = this.id ?: "",
            userId = this.userId ?: "",
            name = this.name ?: "",
            dosage = this.dosage ?: "",
            form = this.form ?: "",
            instructions = this.instructions,
            notes = this.notes,
            quantity = this.quantity ?: 0,
            price = when (val p = this.price) {
                is Double -> p
                is String -> p.toDoubleOrNull()
                is Number -> p.toDouble()
                else -> null
            },
            isActive = this.isActive ?: true,
            photoPath = photoPath
        )
    }

    private fun MedicationEntity.toDomain(): Medication {
        return Medication(
            id = this.id,
            userId = this.userId,
            name = this.name,
            dosage = this.dosage,
            form = this.form,
            instructions = this.instructions,
            notes = this.notes,
            quantity = this.quantity,
            price = this.price,
            isActive = this.isActive,
            photoPath = this.photoPath
        )
    }
}