package com.ict311.task3.data

import java.util.*

class SampleDataProvider {

    companion object {
        private val sampleText1 = "A simple activity"
        private val sampleText2 = "An activity \n with two lines"
        private val sampleText3 = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse convallis lacinia justo, eu varius quam cursus vitae. Aenean et turpis et mi sagittis volutpat vitae nec dolor. Aliquam sed finibus eros, vel pretium mauris. Ut at enim at ipsum cursus aliquam. Etiam vitae velit a purus suscipit congue ac gravida nulla. Suspendisse potenti. In hac habitasse platea dictumst. Vivamus vehicula risus nec mi lobortis, in euismod ligula consectetur. Quisque varius turpis in dolor molestie bibendum. Etiam interdum ex non sapien sagittis mollis. Etiam sem tellus, luctus at purus at, eleifend luctus mi. Nunc vel ipsum et tortor imperdiet feugiat at quis sem. Aliquam a mi id elit convallis tempus. Morbi id dui vel dolor posuere bibendum eget finibus nulla.

            Quisque eget elit interdum, tincidunt tortor eu, porta mi. In porta mi magna, ac bibendum nulla maximus ut. Nullam sed augue mollis, aliquam est non, egestas sapien. Integer volutpat accumsan tellus at fermentum. Integer interdum quis mi vel cursus. Nullam venenatis massa ut pretium hendrerit. Mauris vestibulum tortor in enim iaculis suscipit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Pellentesque consequat ac urna nec tristique. Pellentesque ac arcu risus. Sed a scelerisque enim, at sollicitudin diam.
        """.trimIndent()

        private fun getDate(diff: Long): Date {
            return Date(Date().time + diff)
        }

        fun getSingleActivity(index: Int): ActivityEntity = getActivities()[index]

        fun getActivities() = arrayListOf(
            ActivityEntity(
                UUID.randomUUID(), sampleText1, getDate(0), "place 1",
                1, 5, true
            ),
            ActivityEntity(
                UUID.randomUUID(), sampleText2, getDate(1), "place 2",
                2, 5, true
            ),
            ActivityEntity(
                UUID.randomUUID(), sampleText3, getDate(2), "place 3",
                3, 5, true
            )
        )
    }
}