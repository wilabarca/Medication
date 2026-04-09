package com.example.medication.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.medication.features.searchmedication.domain.usecases.SearchMedicationUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncMedicationsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val searchMedicationUseCases: SearchMedicationUseCases  // ← inyecta el UseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            searchMedicationUseCases.syncMedications()  // ← llama al UseCase del dominio
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "SyncMedicationsWorker"
    }
}