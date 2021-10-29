package com.ict311.task3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * This class represents the application's database.
 * By using the @Database annotation, room library uses this to define the app's database
 *
 * @author Federico Rusconi
 *
 */
@Database(entities = [ActivityEntity::class], version = 1)
@TypeConverters(ActivityTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

}