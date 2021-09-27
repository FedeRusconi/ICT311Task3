package com.ict311.task3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ict311.task3.data.ActivityEntity

class ListUIViewModel : ViewModel() {

    val activitiesList = MutableLiveData<List<ActivityEntity>>()

}