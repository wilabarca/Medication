package com.example.medication.core.hardware.domain

interface DeviceIdProvider {
    fun getDeviceId(): String
}