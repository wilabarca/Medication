package com.example.medication.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medication.features.auth.presentation.screens.LoginScreen
import com.example.medication.features.auth.presentation.screens.RegisterScreen
import com.example.medication.features.medication.presentation.screens.HomeMedicationScreen
import com.example.medication.features.medication.presentation.screens.RegisterMedicationScreen

object AppRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME_MEDICATION = "home_medication"
    const val REGISTER_MEDICATION = "register_medication"
}

@Composable
fun NavigationApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.LOGIN
    ) {
        composable(AppRoutes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AppRoutes.HOME_MEDICATION) {
                        popUpTo(AppRoutes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onRegistrar = {
                    navController.navigate(AppRoutes.REGISTER)
                }
            )
        }

        composable(AppRoutes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(AppRoutes.HOME_MEDICATION) {
            HomeMedicationScreen(
                onNavigateToRegister = {
                    navController.navigate(AppRoutes.REGISTER_MEDICATION)
                }
            )
        }

        composable(AppRoutes.REGISTER_MEDICATION) {
            RegisterMedicationScreen(
                onMedicationRegistered = {
                    navController.popBackStack()
                }
            )
        }
    }
}
