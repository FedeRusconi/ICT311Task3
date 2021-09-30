package com.ict311.task3.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ActivityEntity::class], version = 1)
@TypeConverters(ActivityTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun activityDao(): ActivityDao

}