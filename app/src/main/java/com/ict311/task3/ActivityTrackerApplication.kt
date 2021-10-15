package com.ict311.task3

import android.app.Application
import com.ict311.task3.utils.DB_NAME

class ActivityTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //this.deleteDatabase(DB_NAME)
        DbRepository.initialize(this)
    }
}