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

/**
 * This database repository class is used by the application to connect to the app's database and
 * send requests to the database
 *
 * @author Federico Rusconi
 *
 */
class DbRepository private constructor(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private val activityDao = database.activityDao()
    private val executor = Executors.newSingleThreadExecutor()

    /**
     * Get all activities stored in the database
     */
    fun getAllActivities(): LiveData<List<ActivityEntity>> = activityDao.getAllActivities()

    /**
     * Get a single activity from the database
     * @param id The unique id of the activity to retrieve
     */
    fun getActivityById(id: Int): LiveData<ActivityEntity?> = activityDao.getActivityById(id)

    /**
     * Insert a new activity into the database
     * @param activity The new activity instance to insert
     */
    fun insertActivity(activity: ActivityEntity) {
        executor.execute {
            try {
                activityDao.insertActivity(activity)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    /**
     * Update a selected activity in the database
     * @param activity The activity instance to be updated
     */
    fun updateActivity(activity: ActivityEntity) {
        executor.execute {
            try {
                activityDao.updateActivity(activity)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    /**
     * Delete multiple activities
     * @param selectedActivities A list of activities to delete
     */
    fun deleteActivities(selectedActivities: List<ActivityEntity>) {
        executor.execute {
            try {
                activityDao.deleteActivities(selectedActivities)
            } catch (e: Exception) {
                Log.e(LOG_TAG, e.toString())
            }
        }
    }

    /**
     * Delete a single activity
     * @param selectedActivity The activity instance to delete
     */
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

        /**
         * Initialize the repository
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DbRepository(context)
            }
        }

        /**
         * Get the repository instance
         */
        fun get(): DbRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

}