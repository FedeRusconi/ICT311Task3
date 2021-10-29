package com.ict311.task3.data

import com.ict311.task3.utils.NEW_ACTIVITY_ID
import java.util.*

/**
 * This class is used to create and insert sample data into the database.
 * It can be used for unit testing.
 *
 * @author Federico Rusconi
 *
 */
class SampleDataProvider {

    companion object {
        private const val sampleText1 = "A simple activity"
        private const val sampleText2 = "An activity \n with two lines"
        private val sampleText3 = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse convallis lacinia justo, eu varius quam cursus vitae. Aenean et turpis et mi sagittis volutpat vitae nec dolor. Aliquam sed finibus eros, vel pretium mauris. Ut at enim at ipsum cursus aliquam. Etiam vitae velit a purus suscipit congue ac gravida nulla. Suspendisse potenti. In hac habitasse platea dictumst. Vivamus vehicula risus nec mi lobortis, in euismod ligula consectetur. Quisque varius turpis in dolor molestie bibendum. Etiam interdum ex non sapien sagittis mollis. Etiam sem tellus, luctus at purus at, eleifend luctus mi. Nunc vel ipsum et tortor imperdiet feugiat at quis sem. Aliquam a mi id elit convallis tempus. Morbi id dui vel dolor posuere bibendum eget finibus nulla.

            Quisque eget elit interdum, tincidunt tortor eu, porta mi. In porta mi magna, ac bibendum nulla maximus ut. Nullam sed augue mollis, aliquam est non, egestas sapien. Integer volutpat accumsan tellus at fermentum. Integer interdum quis mi vel cursus. Nullam venenatis massa ut pretium hendrerit. Mauris vestibulum tortor in enim iaculis suscipit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Pellentesque consequat ac urna nec tristique. Pellentesque ac arcu risus. Sed a scelerisque enim, at sollicitudin diam.
        """.trimIndent()

        /**
         * Get current timestamp and add the provided offset
         * @param diff The amount of time (milliseconds) to add to current timestamp
         */
        private fun getDate(diff: Long): Date {
            return Date(Date().time + diff)
        }

        /**
         * Get a single activity
         * @param index The of the activity to retrieve, from below list.
         */
        fun getSingleActivity(index: Int): ActivityEntity = getActivities()[index]

        /**
         * Get three sample activities
         */
        fun getActivities() = arrayListOf(
            ActivityEntity(
                NEW_ACTIVITY_ID, sampleText1, getDate(0), "place 1",
                8.00, 10.00, true
            ),
            ActivityEntity(
                NEW_ACTIVITY_ID, sampleText2, getDate(1), "place 2",
                9.00, 16.00, true
            ),
            ActivityEntity(
                NEW_ACTIVITY_ID, sampleText3, getDate(2), "place 3",
                10.00, 12.00, true
            )
        )
    }
}