package com.ict311.task3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.helpers.LOG_TAG
import com.ict311.task3.helpers.NEW_ACTIVITY_ID

class ItemUIViewModel : ViewModel() {

    private val dbRepository = DbRepository.get()
    private val activityIdLiveData = MutableLiveData<Int>()
    var activityLiveData: LiveData<ActivityEntity?> =
        Transformations.switchMap(activityIdLiveData) { activityId ->
            dbRepository.getActivityById(activityId)
        }

    fun getActivityById(id: Int) {
        activityIdLiveData.value = id
    }

    fun saveUpdateActivity(activity: ActivityEntity): Boolean {
        val returnValue: Boolean
        Log.i(LOG_TAG, activity.title)
        //Execute only if not null value
        when {
            //If it's new activity and empty title, do nothing
            isNewActivity(activity.id) && activity.title.isEmpty() -> {
                returnValue = true
            }
            //If is new activity, create in db
            isNewActivity(activity.id) -> {
                Log.i(LOG_TAG, "create activity")
                dbRepository.insertActivity(activity)
                returnValue = true
            }
            //If not new activity and is empty, return false
            activity.title.isEmpty() -> {
                returnValue = false
            }
            //Else update activity
            else -> {
                Log.i(LOG_TAG, "update activity")
                Log.i(LOG_TAG, activity.title)
                dbRepository.updateActivity(activity)
                returnValue = true
            }
        }
        return returnValue
    }

    private fun isNewActivity(id: Int): Boolean {
        Log.i(LOG_TAG, id.toString())
        return id == NEW_ACTIVITY_ID
    }

}