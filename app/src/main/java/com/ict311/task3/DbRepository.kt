package com.ict311.task3

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.data.AppDatabase
import java.util.*
import java.util.concurrent.Executors

class DbRepository private constructor(context: Context) {

    private val database : AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private val activityDao = database.activityDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllActivities(): LiveData<List<ActivityEntity>>? = activityDao?.getAllActivities()

    fun getActivityById(id: UUID): ActivityEntity? = activityDao?.getActivityById(id)

    fun updateActivity(activity: ActivityEntity) {
        executor.execute {
            activityDao?.updateActivity(activity)
        }
    }

    fun insertActivity(activity: ActivityEntity) {
        executor.execute {
            activityDao?.insertActivity(activity)
        }
    }

    fun deleteActivities(selectedActivities: List<ActivityEntity>) {
        executor.execute {
            activityDao?.deleteActivities(selectedActivities)
        }
    }

    companion object {
        private var INSTANCE: DbRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DbRepository(context)
            }
        }
        fun get(): DbRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}