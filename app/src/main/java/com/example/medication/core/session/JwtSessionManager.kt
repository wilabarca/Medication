package com.example.medication.core.session

import android.content.Context
import android.util.Base64
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

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    fun getUserId(): String? {
        val token = getToken() ?: return null

        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null

            val payload = parts[1]

            val decodedBytes = Base64.decode(
                payload,
                Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
            )

            val json = String(decodedBytes, StandardCharsets.UTF_8)
            val jsonObject = JSONObject(json)

            if (jsonObject.has("id")) jsonObject.getString("id") else null
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val KEY_TOKEN = "auth_token"
    }
}