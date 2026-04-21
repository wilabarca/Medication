package com.example.medication.core.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.favorites.presentation.screens.FavoritesScreen
import com.example.medication.features.medication.domain.entities.Medication
import com.example.medication.features.medication.presentation.screens.AlarmScreens
import com.example.medication.features.medication.presentation.screens.EditMedicationScreen
import com.example.medication.features.medication.presentation.screens.HomeMedicationScreen
import com.example.medication.features.medication.presentation.screens.RegisterAlarmScreen
import com.example.medication.features.medication.presentation.screens.RegisterMedicationScreen
import com.example.medication.features.searchmedication.presentation.screens.SearchMedicinesScreen
import com.google.gson.Gson

@Composable
fun NavigationApp() {
    val navController = rememberNavController()
    val gson = Gson()

    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {
        composable("Login") {
            LoginScreen(
                onCaregiverLoginSuccess = {
                    navController.navigate("Home") {
                        popUpTo("Login") { inclusive = true }
                    }
                },
                onPatientLoginSuccess = {
                    navController.navigate("Home") {
                        popUpTo("Login") { inclusive = true }
                    }
                },
                onRegistrar = {
                    navController.navigate("Register")
                }
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

        composable("Home") {
            HomeMedicationScreen(
                onNavigateToRegister = { navController.navigate("RegisterMedication") },
                onNavigateToSearch = { navController.navigate("SearchMedicines") },
                onNavigateToFavorites = { navController.navigate("Favorites") },
                onNavigateToAlarm = { navController.navigate("Alarms") },
                onNavigateToEdit = { medication ->
                    val json = Uri.encode(gson.toJson(medication))
                    navController.navigate("EditMedication/$json")
                }
            )
        }

        composable("RegisterMedication") {
            RegisterMedicationScreen(
                onMedicationRegistered = { navController.popBackStack() }
            )
        }

        composable("EditMedication/{medication}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("medication") ?: return@composable
            val medication = gson.fromJson(json, Medication::class.java)

            EditMedicationScreen(
                medication = medication,
                onBack = { navController.popBackStack() },
                onUpdated = { navController.popBackStack() }
            )
        }

        composable("SearchMedicines") {
            SearchMedicinesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("Favorites") {
            FavoritesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("Alarms") {
            AlarmScreens(
                onBack = { navController.popBackStack() },
                onAddAlarm = { navController.navigate("RegisterAlarm") }
            )
        }

        composable("RegisterAlarm") {
            RegisterAlarmScreen(
                onBack = { navController.popBackStack() },
                onAlarmSaved = { navController.popBackStack() }
            )
        }
    }
}