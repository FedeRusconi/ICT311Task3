package com.ict311.task3.data

import java.util.*

data class ActivityEntity (
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var Date: Date = Date(),
    var place: String = "",
    var startTime: Int,
    var endTime: Int,
    var isIndividual: Boolean = true
)