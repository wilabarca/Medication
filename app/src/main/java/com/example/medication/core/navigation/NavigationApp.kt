package com.example.medication.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.searchmedicines.presentation.screens.SearchMedicinesScreen
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.favorites.presentation.screens.FavoritesScreen
import com.example.medication.features.medication.presentation.screens.HomeMedicationScreen
import com.example.medication.features.medication.presentation.screens.RegisterMedicationScreen

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "Login"
    ) {

        composable("Login") {
            LoginScreen(
                onLoginSuccess = {
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
                onNavigateToRegister = {
                    navController.navigate("RegisterMedication")
                },
                onNavigateToSearch = {
                    navController.navigate("Search")
                },
                onNavigateToFavorites = {
                    navController.navigate("Favorites")
                }
            )
        }

        composable("RegisterMedication") {
            RegisterMedicationScreen(
                onMedicationRegistered = {
                    navController.popBackStack()
                }
            )
        }

        composable("Search") {
            SearchMedicinesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("Favorites") {
            FavoritesScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}