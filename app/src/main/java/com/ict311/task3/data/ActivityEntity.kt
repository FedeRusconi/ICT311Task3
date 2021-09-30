package com.ict311.task3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ict311.task3.helpers.NEW_ACTIVITY_ID
import java.util.*

@Entity(tableName = "activity")
data class ActivityEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = NEW_ACTIVITY_ID,
    var title: String = "",
    var date: Date = Date(),
    var place: String = "",
    var startTime: Int = 0,
    var endTime: Int = 0,
    var isIndividual: Boolean = true
)