package com.ict311.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.utils.NEW_ACTIVITY_ID

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
        //Execute only if not null value
        when {
            //If it's new activity and empty title, do nothing
            isNewActivity(activity.id) && activity.title.isEmpty() -> {
                returnValue = true
            }
            //If is new activity, create in db
            isNewActivity(activity.id) -> {
                dbRepository.insertActivity(activity)
                returnValue = true
            }
            //If not new activity and is empty, return false
            activity.title.isEmpty() -> {
                returnValue = false
            }
            //Else update activity
            else -> {
                dbRepository.updateActivity(activity)
                returnValue = true
            }
        }
        return returnValue
    }

    fun deleteActivityById(selectedActivity: ActivityEntity) {
        dbRepository.deleteActivityById(selectedActivity)
    }

    private fun isNewActivity(id: Int): Boolean {
        return id == NEW_ACTIVITY_ID
    }

}