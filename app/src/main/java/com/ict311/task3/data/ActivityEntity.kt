package com.ict311.task3.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ict311.task3.utils.NEW_ACTIVITY_ID
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * This class represents the Activity entity that is stored in the database
 * By using the @Entity annotation, room library uses this class to define the
 * database's only table.
 *
 * @author Federico Rusconi
 *
 */
@Parcelize
@Entity(tableName = "activity")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = NEW_ACTIVITY_ID,
    var title: String = "",
    var date: Date = Date(),
    var place: String = "",
    var startTime: Double = 0.00,
    var endTime: Double = 0.00,
    var isGroup: Boolean = false
) : Parcelable, Comparable<ActivityEntity?>, Cloneable {

    /**
     * Compare current instance with other ActivityEntity
     * @param other The other ActivityInstance to compare to current one
     * @return 0 if two objects are equal or 1 if they are not equal
     */
    override fun compareTo(other: ActivityEntity?): Int {
        return if (
            id == other?.id
            && title == other.title
            && date.compareTo(other.date) == 0
            && place == other.place
            && startTime == other.startTime
            && endTime == other.endTime
            && isGroup == other.isGroup
        ) 0 else 1
    }

    /**
     * Clone activity
     */
    public override fun clone(): ActivityEntity {
        return super.clone() as ActivityEntity
    }

    /**
     * Check if current instance is empty
     * @return True if empty
     */
    fun isEmpty(): Boolean {
        return (
            title.isEmpty()
            && place.isEmpty()
            && startTime == 0.00
            && endTime == 0.00
        )
    }

    /**
     * Validate current instance
     * @return True if valid
     */
    fun validate(): Boolean {
        return (
            title.isNotEmpty()
            && place.isNotEmpty()
            && (startTime != 0.00 || endTime != 0.00)
        )
    }
}