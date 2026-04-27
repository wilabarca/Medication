package com.example.medication.core.navigation

object Routes {
    const val LOGIN = "Login"
    const val REGISTER = "Register"
    const val HOME = "Home"

    const val PATIENTS = "Patients"
    const val CREATE_PATIENT = "CreatePatient"

    const val LINK_PATIENTS = "LinkPatients"

    const val PATIENT_MEDICATIONS = "PatientMedications/{patientId}"
    const val REGISTER_MEDICATION = "RegisterMedication/{patientId}"
    const val EDIT_MEDICATION = "EditMedication/{medication}"

    const val SEARCH_MEDICINES = "SearchMedicines"
    const val FAVORITES = "Favorites"

    const val ALARMS = "Alarms"
    const val REGISTER_ALARM = "RegisterAlarm"

    fun patientMedications(patientId: String): String {
        return "PatientMedications/$patientId"
    }

    fun registerMedication(patientId: String): String {
        return "RegisterMedication/$patientId"
    }

    fun editMedication(medicationJson: String): String {
        return "EditMedication/$medicationJson"
    }
}