package com.example.medication.features.medication.data.datasources.local.converters

import androidx.room.TypeConverter

class AlarmConverters {

    @TypeConverter  // ✅ @TypeConverter no @TypeConverters
    fun fromWeekDays(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter  // ✅
    fun toWeekDays(value: String): List<Int> {
        if (value.isBlank()) return emptyList()
        return value.split(",").map { it.toInt() }
    }
}