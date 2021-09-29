package com.ict311.task3.data

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

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
    fun getActivityById(id: UUID): ActivityEntity?

    @Query("SELECT COUNT(*) FROM activity")
    fun getCount(): Int

    @Query("DELETE FROM activity")
    fun deleteAll(): Int

    @Delete
    fun deleteActivities(selectedActivities: List<ActivityEntity>): Int


}