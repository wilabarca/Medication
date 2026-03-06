package com.example.medication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.medication.core.theme.MedicationTheme
import com.example.medication.features.Auth.presentation.screens.LoginScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var isLoggedIn by remember { mutableStateOf(false) }

                    if (isLoggedIn) {
                        // TODO: aquí va tu pantalla principal después del login
                    } else {
                        LoginScreen(
                            onLoginSuccess = { isLoggedIn = true },
                            onRegistrar = {
                                // TODO: navegar a pantalla de registro
                            }
                        )
                    }
                }
            }
        }
    }
}