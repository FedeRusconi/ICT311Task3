package com.ict311.task3

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ict311.task3.utils.*
import java.util.*
import kotlin.math.floor
import kotlin.math.round

class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minutes: Int,  ->
                val resultTime: Double = hour + (minutes.toDouble() / 100)
                val dialogKey = when(args.startOrEnd) {
                    "start" -> DIALOG_TIME_START_KEY
                    "end" -> DIALOG_TIME_END_KEY
                    else -> ""
                }
                Helpers.setNavigationResult(findNavController(), dialogKey, resultTime)
            }

        val hours = floor(args.activityTime).toInt()
        val minutes = round(100 * (args.activityTime - hours)).toInt()

        return TimePickerDialog(
            requireContext(),
            timeListener,
            hours,
            minutes,
            true
        )
    }

}