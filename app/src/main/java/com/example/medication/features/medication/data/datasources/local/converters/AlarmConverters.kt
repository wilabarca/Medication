package com.example.medication.features.medication.data.datasources.local.converters

import androidx.room.TypeConverters


class AlarmConverters{
    @TypeConverters
    fun fromWeekDays(value: List<Int>): String{
        return value.joinToString(separator = ",")
    }
    @TypeConverters
    fun toWeekDays(value: String): List<Int>{
        if (value.isBlank()) return emptyList()
        return value.split(",").map {it.toInt()}
    }
}