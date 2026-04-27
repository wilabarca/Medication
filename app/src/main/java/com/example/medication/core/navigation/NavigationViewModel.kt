package com.example.medication.core.navigation

import androidx.lifecycle.ViewModel
import com.example.medication.core.session.JwtSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val jwtSessionManager: JwtSessionManager
) : ViewModel() {

    fun getUserId(): String {
        return jwtSessionManager.getUserId().orEmpty()
    }

    fun getUserRole(): String {
        return jwtSessionManager.getRole().orEmpty()
    }

    fun isLoggedIn(): Boolean {
        return jwtSessionManager.isLoggedIn()
    }
}