package com.example.medication.core.session

import android.content.Context
import android.util.Base64
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JwtSessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    // ── Token JWT ──────────────────────────────────────────────────────────────
    fun saveToken(token: String) {
        Log.d("JWT_DEBUG", "saveToken -> $token")
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        val token = prefs.getString(KEY_TOKEN, null)
        Log.d("JWT_DEBUG", "getToken -> $token")
        return token
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    // ── Decode payload ─────────────────────────────────────────────────────────
    private fun decodePayload(): JSONObject? {
        val token = getToken() ?: return null
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            val decodedBytes = Base64.decode(
                parts[1],
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )
            val json = String(decodedBytes, StandardCharsets.UTF_8)
            Log.d("JWT_DEBUG", "payload decoded -> $json")
            JSONObject(json)
        } catch (e: Exception) {
            Log.e("JWT_DEBUG", "Error decoding token", e)
            null
        }
    }

    fun getUserId(): String? {
        val payload = decodePayload() ?: return null
        val userId = if (payload.has("id")) payload.getString("id") else null
        Log.d("JWT_DEBUG", "userId -> $userId")
        return userId
    }

    fun getRole(): String? {
        val payload = decodePayload() ?: return null
        val role = when {
            payload.has("role")  -> payload.getString("role")
            payload.has("roles") -> payload.getString("roles")
            else -> null
        }
        Log.d("JWT_DEBUG", "role -> $role")
        return role
    }

    fun isLoggedIn(): Boolean = getToken() != null

    // ── Paciente vinculado ─────────────────────────────────────────────────────
    fun saveLinkedPatientId(patientId: String) {
        Log.d("JWT_DEBUG", "saveLinkedPatientId -> $patientId")
        prefs.edit().putString(KEY_LINKED_PATIENT_ID, patientId).apply()
    }

    fun getLinkedPatientId(): String? {
        val id = prefs.getString(KEY_LINKED_PATIENT_ID, null)
        Log.d("JWT_DEBUG", "getLinkedPatientId -> $id")
        return id
    }

    fun clearLinkedPatientId() {
        prefs.edit().remove(KEY_LINKED_PATIENT_ID).apply()
    }

    // ── Cerrar sesión completo ─────────────────────────────────────────────────
    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_TOKEN             = "auth_token"
        private const val KEY_LINKED_PATIENT_ID = "linked_patient_id"
    }
}