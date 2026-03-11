package com.example.medication.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Login") {
        composable("Login") {
            LoginScreen(
                onLoginSuccess = { navController.navigate("Home") },
                onRegistrar = { navController.navigate("Register") }
            )
        }
        composable("Register") {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate("Login") }
            )
        }
    }
}