package com.example.medication.features.searchmedicines.domain.usecases

import com.example.medication.features.searchmedicines.domain.entities.Medicine
import com.example.medication.features.searchmedicines.domain.repositories.MedicineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchMedicinesUseCase @Inject constructor(
    private val repository: MedicineRepository
) {
    operator fun invoke(query: String): Flow<List<Medicine>> {
        if (query.isBlank() || query.length < 2) return flow { emit(emptyList()) }
        return repository.searchMedicines(query.trim())
    }
}