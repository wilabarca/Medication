package com.example.medication.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.caregiver.presentation.screens.CaregiverHomeScreen
import com.example.medication.features.favorites.presentation.screens.FavoritesScreen
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.screens.AlarmScreens
import com.example.medication.features.medication.presentation.screens.EditMedicationScreen
import com.example.medication.features.medication.presentation.screens.HomeMedicationScreen
import com.example.medication.features.medication.presentation.screens.RegisterAlarmScreen
import com.example.medication.features.medication.presentation.screens.RegisterMedicationScreen
import com.example.medication.features.patients.domain.entities.Patient
import com.example.medication.features.patients.presentation.screens.CreatePatientScreen
import com.example.medication.features.patients.presentation.screens.PatientsListScreen
import com.example.medication.features.searchmedication.presentation.screens.SearchMedicinesScreen
import com.google.gson.Gson

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(
        navController    = navController,
        startDestination = "Login"
    ) {

        // ── Auth ───────────────────────────────────────────────────────────────
        composable("Login") {
            LoginScreen(
                onCaregiverLoginSuccess = {
                    // Cuidador → su propio home
                    navController.navigate("CaregiverHome") {
                        popUpTo("Login") { inclusive = true }
                    }
                },
                onPatientLoginSuccess = {
                    // Paciente → home de medicamentos
                    navController.navigate("PatientHome") {
                        popUpTo("Login") { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate("Register") }
            )
        }

        composable("Register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("Login") {
                        popUpTo("Register") { inclusive = true }
                    }
                }
            )
        }

        // ── Home Paciente ──────────────────────────────────────────────────────
        composable("PatientHome") {
            HomeMedicationScreen(
                onNavigateToRegister  = { navController.navigate("RegisterMedication") },
                onNavigateToSearch    = { navController.navigate("SearchMedicines") },
                onNavigateToFavorites = { navController.navigate("Favorites") },
                onNavigateToAlarm     = { navController.navigate("Alarms") },
                onNavigateToEdit      = { medication ->
                    val json = Uri.encode(gson.toJson(medication))
                    navController.navigate("EditMedication/$json")
                }
            )
        }

        // ── Home Cuidador ──────────────────────────────────────────────────────
        composable("CaregiverHome") {
            CaregiverHomeScreen(
                onNavigateToCreatePatient = { navController.navigate("CreatePatient") },
                onNavigateToPatientDetail = { patient ->
                    val json = Uri.encode(gson.toJson(patient))
                    navController.navigate("PatientDetail/$json")
                }
            )
        }

        // Detalle de un paciente (vista del cuidador — ve los meds del paciente)
        composable("PatientDetail/{patient}") { backStackEntry ->
            val json    = backStackEntry.arguments?.getString("patient") ?: return@composable
            val patient = gson.fromJson(json, Patient::class.java)
            PatientsListScreen(
                caregiverUserId   = patient.caregiverUserId,
                onCreatePatient   = { navController.navigate("CreatePatient") },
                onPatientSelected = { selected ->
                    val selectedJson = Uri.encode(gson.toJson(selected))
                    navController.navigate("PatientDetail/$selectedJson")
                }
            )
        }

        // Crear paciente (desde el home del cuidador)
        composable("CreatePatient") {
            CreatePatientScreen(
                onBack    = { navController.popBackStack() },
                onCreated = { navController.popBackStack() }
            )
        }

        // ── Medicamentos ───────────────────────────────────────────────────────
        composable("RegisterMedication") {
            RegisterMedicationScreen(
                onMedicationRegistered = { navController.popBackStack() }
            )
        }

        composable("EditMedication/{medication}") { backStackEntry ->
            val json       = backStackEntry.arguments?.getString("medication") ?: return@composable
            val medication = gson.fromJson(json, Medication::class.java)
            EditMedicationScreen(
                medication = medication,
                onBack     = { navController.popBackStack() },
                onUpdated  = { navController.popBackStack() }
            )
        }

        // ── Otras pantallas ────────────────────────────────────────────────────
        composable("SearchMedicines") {
            SearchMedicinesScreen(onBack = { navController.popBackStack() })
        }

        composable("Favorites") {
            FavoritesScreen(onBack = { navController.popBackStack() })
        }

        composable("Alarms") {
            AlarmScreens(
                onBack     = { navController.popBackStack() },
                onAddAlarm = { navController.navigate("RegisterAlarm") }
            )
        }

        composable("RegisterAlarm") {
            RegisterAlarmScreen(
                onBack       = { navController.popBackStack() },
                onAlarmSaved = { navController.popBackStack() }
            )
        }
    }
}