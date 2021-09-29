package com.ict311.task3.data

import androidx.room.TypeConverter
import java.util.*

class ActivityTypeConverters {

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date?.time
    }
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

}