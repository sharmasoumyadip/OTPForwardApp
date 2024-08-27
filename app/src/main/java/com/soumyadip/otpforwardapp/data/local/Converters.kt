package com.soumyadip.otpforwardapp.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListToString(list: List<String>): String {
        if (list.isEmpty()) return ""
        return list.joinToString(separator = ";")
    }

    @TypeConverter
    fun fromStringToList(s: String?): List<String> {
        if (s.isNullOrEmpty()) return emptyList()
        return s.split(";").filter { it.isNotBlank() || it.isNotEmpty() }
    }
}