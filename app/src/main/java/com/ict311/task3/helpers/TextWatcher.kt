package com.ict311.task3.helpers

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.ict311.task3.R
import com.ict311.task3.data.ActivityEntity

class TextWatcher (var view: View, var activity: ActivityEntity) : TextWatcher {

    override fun beforeTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
        when (view.id) {
            R.id.activityTitle -> {
                Log.i(LOG_TAG, "onTextChanged called")
                activity.title = sequence.toString()
                Log.i(LOG_TAG, activity.title)
            }
            R.id.activityPlace -> {
                activity.place = sequence.toString()
            }
        }
    }

    override fun afterTextChanged(sequence: Editable?) {}
}