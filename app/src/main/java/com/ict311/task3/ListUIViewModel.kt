package com.ict311.task3

import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity

/**
 * This view model controller class sits in the middle between the ListUI fragment controller class
 * and the model class (Activity Entity).
 * It is used to avoid overloading the fragment class with both UI management functionalities
 * and data management functionalities.
 *
 * @author Federico Rusconi
 *
 */
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