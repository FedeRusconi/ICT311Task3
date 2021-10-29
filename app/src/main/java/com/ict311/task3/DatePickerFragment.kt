package com.ict311.task3

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ict311.task3.utils.DIALOG_DATE_KEY
import com.ict311.task3.utils.Helpers
import java.util.*

/**
 * This class is used to create, display and manage a date picker dialog,
 * allowing users to select a date for their activities
 *
 * @author Federico Rusconi
 *
 */
class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                //Capture selected date and send it back to fragment
                val resultDate: Date = GregorianCalendar(year, month, day).time
                Helpers.setNavigationResult(findNavController(), DIALOG_DATE_KEY, resultDate)
            }
        //Get and set default date
        val date = Date(args.activityDate)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

}