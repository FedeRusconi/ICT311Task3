package com.ict311.task3

import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity

class ListUIViewModel : ViewModel() {

    private val dbRepository = DbRepository.get()

    val activitiesList = dbRepository.getAllActivities()

    /**
     * Delete selected activities
     * @param selectedActivities The list of activities to delete
     */
    fun deleteActivities(selectedActivities: List<ActivityEntity>) {
        dbRepository.deleteActivities(selectedActivities)
    }

}