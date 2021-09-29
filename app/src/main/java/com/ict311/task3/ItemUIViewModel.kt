package com.ict311.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity
import java.util.*

class ItemUIViewModel : ViewModel() {

    private val dbRepository = DbRepository.get()
    val currentActivity = MutableLiveData<ActivityEntity>()

    fun getActivityById(id: UUID) {
        val selectedActivity = dbRepository.getActivityById(id)
        currentActivity.postValue(selectedActivity!!)
    }

    fun saveUpdateActivity() : Boolean {
        var returnValue = false
        //Execute only if not null value
        currentActivity.value?.let {
            it.title = it.title.trim()
            when {
                //If it's new activity and empty title, do nothing
                isNewActivity(it.id) && it.title.isEmpty() -> {
                    returnValue = true
                }
                //If is new activity, create in db
                isNewActivity(it.id) -> {
                    dbRepository.insertActivity(it)
                    returnValue = true
                }
                //If not new activity and is empty, return false
                it.title.isEmpty() -> {
                    returnValue = false
                }
                //Else update activity
                else -> {
                    dbRepository.updateActivity(it)
                    returnValue = true
                }
            }
        }
        return returnValue
    }

    private fun isNewActivity(id: UUID) : Boolean {
        return id == UUID.fromString(NEW_ACTIVITY_ID)
    }

}