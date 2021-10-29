package com.ict311.task3

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ict311.task3.utils.DIALOG_TIME_END_KEY
import com.ict311.task3.utils.DIALOG_TIME_START_KEY
import com.ict311.task3.utils.Helpers
import kotlin.math.floor
import kotlin.math.round

/**
 * This class is used to create, display and manage a time picker dialog,
 * allowing users to select a time (start or end) for their activities
 *
 * @author Federico Rusconi
 *
 */
class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minutes: Int ->
                //Capture selected time and send it back to fragment
                val resultTime: Double = hour + (minutes.toDouble() / 100)
                val dialogKey = when(args.startOrEnd) {
                    "start" -> DIALOG_TIME_START_KEY
                    "end" -> DIALOG_TIME_END_KEY
                    else -> ""
                }
                Helpers.setNavigationResult(findNavController(), dialogKey, resultTime)
            }

        //Get and set default date
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