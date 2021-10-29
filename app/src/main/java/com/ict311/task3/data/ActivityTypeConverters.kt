package com.ict311.task3.data

import androidx.room.TypeConverter
import java.util.*

/**
 * This class is used to convert reference types that are not supported by the database
 * into primitive types, and vice versa.
 *
 * @author Federico Rusconi
 *
 */
class ActivityTypeConverters {

    /**
     * Convert from Date type to numerical
     *@param date The Date instance to convert
     */
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    /**
     * Convert from numerical to Date type
     *@param millisSinceEpoch The numerical date to convert
     */
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }

}