package com.ict311.task3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.data.SampleDataProvider

class ListUIViewModel : ViewModel() {

    private val dbRepository = DbRepository.get()

    val activitiesList = dbRepository.getAllActivities()

    fun addActivity(activity: ActivityEntity) {
        dbRepository.insertActivity(activity)
    }

    fun deleteActivities(selectedActivities: List<ActivityEntity>) {
        dbRepository.deleteActivities(selectedActivities)
    }

}