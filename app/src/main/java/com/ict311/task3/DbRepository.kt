package com.ict311.task3

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.data.AppDatabase
import com.ict311.task3.utils.DB_NAME
import com.ict311.task3.utils.LOG_TAG
import java.util.concurrent.Executors

class DbRepository private constructor(context: Context) {

    private val database : AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private val activityDao = database.activityDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAllActivities(): LiveData<List<ActivityEntity>> = activityDao.getAllActivities()

    fun getActivityById(id: Int): LiveData<ActivityEntity?> = activityDao.getActivityById(id)

    fun insertActivity(activity: ActivityEntity) {
        executor.execute {
            try {
                activityDao.insertActivity(activity)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    fun updateActivity(activity: ActivityEntity) {
        executor.execute {
            try {
                activityDao.updateActivity(activity)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    fun deleteActivities(selectedActivities: List<ActivityEntity>) {
        executor.execute {
            try {
                activityDao.deleteActivities(selectedActivities)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    fun deleteActivityById(selectedActivity: ActivityEntity) {
        executor.execute {
            try {
                activityDao.deleteActivityById(selectedActivity)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
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