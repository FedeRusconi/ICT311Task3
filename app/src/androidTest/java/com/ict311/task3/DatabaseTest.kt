package com.ict311.task3

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ict311.task3.data.ActivityDao
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.data.AppDatabase
import com.ict311.task3.data.SampleDataProvider
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.*

@RunWith(AndroidJUnit4::class)
class DatabaseTest {

    private lateinit var dao: ActivityDao
    private lateinit var database: AppDatabase


    @Before
    fun createDb() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.activityDao()!!
    }

    @Test
    fun createActivities() {
        dao.insertAll(SampleDataProvider.getActivities())
        val count = dao.getCount()
        assertEquals(SampleDataProvider.getActivities().size, count)
    }

    @Test
    fun createActivity() {
        dao.insertActivity(SampleDataProvider.getSingleActivity(0))
        val count = dao.getCount()
        assertEquals(1, count)
    }

    @Test
    fun getActivityById() {
        val id = UUID.randomUUID()
        val activity = ActivityEntity(
            id,
            "Test Activity",
            Date()
        )
        dao.insertActivity(activity)
        assertEquals(id, dao.getActivityById(id)?.id)
    }

    @After
    fun closeDb() {
        database.close()
    }
}