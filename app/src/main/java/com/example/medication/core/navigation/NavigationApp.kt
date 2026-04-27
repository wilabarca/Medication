package com.example.medication.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.favorites.presentation.screens.FavoritesScreen
import com.example.medication.features.home.presentation.screens.HomeScreen
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.screens.AlarmScreens
import com.example.medication.features.medication.presentation.screens.EditMedicationScreen
import com.example.medication.features.medication.presentation.screens.HomeMedicationScreen
import com.example.medication.features.medication.presentation.screens.RegisterAlarmScreen
import com.example.medication.features.medication.presentation.screens.RegisterMedicationScreen
import com.example.medication.features.patients.presentation.screens.CreatePatientScreen
import com.example.medication.features.patients.presentation.screens.PatientsListScreen
import com.example.medication.features.searchmedication.presentation.screens.SearchMedicinesScreen
import com.google.gson.Gson

@Composable
fun NavigationApp(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val gson = remember { Gson() }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onCaregiverLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onPatientLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onRegistrar = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            val userId = navigationViewModel.getUserId()
            val role = navigationViewModel.getUserRole()

            HomeScreen(
                role = role,
                userId = userId,
                linkedPatientId = null,
                onOpenPatientLinking = {
                    navController.navigate(Routes.LINK_PATIENTS)
                },
                onOpenPatients = {
                    navController.navigate(Routes.PATIENTS)
                },
                onCreatePatient = {
                    navController.navigate(Routes.CREATE_PATIENT)
                },
                onOpenPatientMedications = { patientId ->
                    navController.navigate(Routes.patientMedications(patientId))
                },
                onOpenPatientAlarms = {
                    navController.navigate(Routes.ALARMS)
                }
            )
        }

        composable(Routes.PATIENTS) {
            val caregiverUserId = navigationViewModel.getUserId()

            PatientsListScreen(
                caregiverUserId = caregiverUserId,
                onCreatePatient = {
                    navController.navigate(Routes.CREATE_PATIENT)
                },
                onPatientSelected = { patient ->
                    navController.navigate(Routes.patientMedications(patient.id))
                }
            )
        }

        composable(Routes.CREATE_PATIENT) {
            val caregiverUserId = navigationViewModel.getUserId()

            CreatePatientScreen(
                caregiverUserId = caregiverUserId,
                onCreated = {
                    navController.navigate(Routes.PATIENTS) {
                        popUpTo(Routes.PATIENTS) {
                            inclusive = true
                        }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.PATIENT_MEDICATIONS,
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
                ?: return@composable

            HomeMedicationScreen(
                patientId = patientId,
                canEdit = true,
                onNavigateToRegister = { selectedPatientId ->
                    navController.navigate(Routes.registerMedication(selectedPatientId))
                },
                onNavigateToSearch = {
                    navController.navigate(Routes.SEARCH_MEDICINES)
                },
                onNavigateToFavorites = {
                    navController.navigate(Routes.FAVORITES)
                },
                onNavigateToAlarm = {
                    navController.navigate(Routes.ALARMS)
                },
                onNavigateToEdit = { medication ->
                    val medicationJson = Uri.encode(gson.toJson(medication))
                    navController.navigate(Routes.editMedication(medicationJson))
                }
            )
        }

        composable(
            route = Routes.REGISTER_MEDICATION,
            arguments = listOf(
                navArgument("patientId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")
                ?: return@composable

            RegisterMedicationScreen(
                patientId = patientId,
                onMedicationRegistered = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.EDIT_MEDICATION,
            arguments = listOf(
                navArgument("medication") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val medicationJson = backStackEntry.arguments?.getString("medication")
                ?: return@composable

            val medication = gson.fromJson(
                medicationJson,
                Medication::class.java
            )

            EditMedicationScreen(
                medication = medication,
                onBack = {
                    navController.popBackStack()
                },
                onUpdated = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SEARCH_MEDICINES) {
            SearchMedicinesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.ALARMS) {
            AlarmScreens(
                onBack = {
                    navController.popBackStack()
                },
                onAddAlarm = {
                    navController.navigate(Routes.REGISTER_ALARM)
                }
            )
        }

        composable(Routes.REGISTER_ALARM) {
            RegisterAlarmScreen(
                onBack = {
                    navController.popBackStack()
                },
                onAlarmSaved = {
                    navController.popBackStack()
                }
            )
        }
    }
}