package com.ict311.task3

import android.app.Application

class ActivityTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DbRepository.initialize(this)
    }
}