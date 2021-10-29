package com.ict311.task3

import android.app.Application

/**
 * This class is instantiated when the application is launched
 * and it is used to initialize the singleton database repository.
 *
 * @author Federico Rusconi
 *
 */
class ActivityTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DbRepository.initialize(this)
    }
}