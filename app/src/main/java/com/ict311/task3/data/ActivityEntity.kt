package com.ict311.task3.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ict311.task3.utils.NEW_ACTIVITY_ID
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "activity")
data class ActivityEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int = NEW_ACTIVITY_ID,
    var title: String = "",
    var date: Date = Date(),
    var place: String = "",
    var startTime: Int = 0,
    var endTime: Int = 0,
    var isGroup: Boolean = false
): Parcelable