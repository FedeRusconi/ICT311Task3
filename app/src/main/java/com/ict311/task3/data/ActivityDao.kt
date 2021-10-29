package com.ict311.task3.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * This Data Access Object interface is used to map application calls
 * to database operations
 *
 * @author Federico Rusconi
 *
 */
@Dao
interface ActivityDao {

    @Insert
    fun insertActivity(activity: ActivityEntity)

    @Insert
    fun insertAll(activities: List<ActivityEntity>)

    @Update
    fun updateActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activity ORDER BY date ASC")
    fun getAllActivities(): LiveData<List<ActivityEntity>>

    @Query("SELECT * FROM activity WHERE id = :id")
    fun getActivityById(id: Int): LiveData<ActivityEntity?>

    @Query("SELECT COUNT(*) FROM activity")
    fun getCount(): Int

    @Query("DELETE FROM activity")
    fun deleteAll(): Int

    @Delete
    fun deleteActivities(selectedActivities: List<ActivityEntity>): Int

    @Delete
    fun deleteActivityById(selectedActivity: ActivityEntity)


}