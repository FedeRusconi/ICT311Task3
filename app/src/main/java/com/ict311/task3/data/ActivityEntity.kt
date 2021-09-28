package com.ict311.task3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "activity")
data class ActivityEntity (
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var place: String = "",
    var startTime: Int,
    var endTime: Int,
    var isIndividual: Boolean = true
)