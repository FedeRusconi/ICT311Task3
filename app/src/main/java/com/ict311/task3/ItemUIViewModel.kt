package com.ict311.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.utils.Helpers

/**
 * This view model controller class sits in the middle between the ItemUI fragment controller class
 * and the model class (Activity Entity).
 * It is used to avoid overloading the fragment class with both UI management functionalities
 * and data management functionalities.
 *
 * @author Federico Rusconi
 *
 */
class ItemUIViewModel : ViewModel() {

    private val dbRepository = DbRepository.get()
    private val activityIdLiveData = MutableLiveData<Int>()
    var activityLiveData: LiveData<ActivityEntity?> =
        Transformations.switchMap(activityIdLiveData) { activityId ->
            dbRepository.getActivityById(activityId)
        }
    var activityChangeDetect: ActivityEntity? = null

    /**
     * Find activity by ID
     * @param id The activity ID
     */
    fun getActivityById(id: Int) {
        activityIdLiveData.value = id
    }

    /**
     * Save or update selected activity
     * @param activity The selected activity
     * @return 1 - Successful. 0 - No response. -1 - Error
     */
    fun saveUpdateActivity(activity: ActivityEntity): Int {
        val returnValue: Int
        //Execute only if not null value
        when {
            //If it's new activity and empty, do nothing
            isNewActivity(activity.id) && activity.isEmpty() -> {
                returnValue = 0
            }
            //If not new activity and is not valid, return error
            !activity.validate() -> {
                returnValue = -1
            }
            //If is new activity, create in db
            isNewActivity(activity.id) -> {
                dbRepository.insertActivity(activity)
                returnValue = 1
            }
            //Else update activity
            else -> {
                dbRepository.updateActivity(activity)
                returnValue = 1
            }
        }
        return returnValue
    }

    /**
     * Delete selected activity
     * @param selectedActivity The activity to be removed
     */
    fun deleteActivityById(selectedActivity: ActivityEntity) {
        dbRepository.deleteActivityById(selectedActivity)
    }

    /**
     * Check if activity is new
     * @param id The activity ID
     */
    private fun isNewActivity(id: Int): Boolean {
        return Helpers.isNewActivity(id)
    }

}