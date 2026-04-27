package com.example.medication.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.auth.presentation.screens.RoleSelectorScreen
import com.example.medication.features.caregiver.presentation.screens.CaregiverHomeScreen
import com.example.medication.features.history.presentation.screens.HistoryScreen
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
    val gson          = Gson()

    NavHost(
        navController    = navController,
        startDestination = "RoleSelector"
    ) {

        // ── Selector de rol ────────────────────────────────────────────────────
        composable("RoleSelector") {
            RoleSelectorScreen(
                // Cuidador → Login normal
                onCaregiverSelected = { navController.navigate("Login/caregiver") },
                // Paciente → directo al Home, sin login
                onPatientSelected   = {
                    navController.navigate("PatientHome") {
                        popUpTo("RoleSelector") { inclusive = false }
                    }
                }
            )
        }

        // ── Auth (solo cuidador) ───────────────────────────────────────────────
        composable("Login/{expectedRole}") { backStackEntry ->
            val expectedRole = backStackEntry.arguments?.getString("expectedRole") ?: "caregiver"
            LoginScreen(
                onCaregiverLoginSuccess = {
                    navController.navigate("CaregiverHome") {
                        popUpTo("RoleSelector") { inclusive = true }
                    }
                },
                // El paciente nunca llega aquí, pero por si acaso
                onPatientLoginSuccess = {
                    navController.navigate("PatientHome") {
                        popUpTo("RoleSelector") { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate("Register/$expectedRole") }
            )
        }

        // Ruta de login sin rol (fallback)
        composable("Login") {
            LoginScreen(
                onCaregiverLoginSuccess = {
                    navController.navigate("CaregiverHome") {
                        popUpTo("RoleSelector") { inclusive = true }
                    }
                },
                onPatientLoginSuccess = {
                    navController.navigate("PatientHome") {
                        popUpTo("RoleSelector") { inclusive = true }
                    }
                },
                onRegistrar = { navController.navigate("Register/caregiver") }
            )
        }

        composable("Register/{expectedRole}") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("Login") {
                        popUpTo("Register/{expectedRole}") { inclusive = true }
                    }
                }
            )
        }

        // ── Home Paciente ──────────────────────────────────────────────────────
        // El paciente llega aquí directamente desde RoleSelector.
        // La vinculación con el cuidador ocurre DENTRO de esta pantalla
        // a través del modal de token.
        composable("PatientHome") {
            HomeMedicationScreen(
                onNavigateToSearch   = { navController.navigate("SearchMedicines") },
                onNavigateToHistory  = { navController.navigate("History") },
                onNavigateToEdit     = { medication ->
                    // readOnly=true en la card, así que este callback no se activa,
                    // pero se deja por si se habilita edición en el futuro.
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

        composable("CreatePatient") {
            CreatePatientScreen(
                onBack    = { navController.popBackStack() },
                onCreated = { navController.popBackStack() }
            )
        }

        // ── Medicamentos (solo usadas por el cuidador) ─────────────────────────
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

        // ── Compartidas ────────────────────────────────────────────────────────
        composable("History") {
            HistoryScreen(onBack = { navController.popBackStack() })
        }

        composable("SearchMedicines") {
            SearchMedicinesScreen(onBack = { navController.popBackStack() })
        }

        // ── Alarmas (solo cuidador, pero se deja ruteada por si acaso) ─────────
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