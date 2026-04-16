package com.example.medication.core.hardware.data

import android.content.Context
import android.provider.Settings
import com.example.medication.core.hardware.domain.DeviceIdProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidDeviceIdProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : DeviceIdProvider {

    override fun getDeviceId(): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown_device"
    }
}