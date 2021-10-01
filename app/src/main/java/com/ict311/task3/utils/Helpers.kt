package com.ict311.task3.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.viewbinding.ViewBinding

class Helpers {

    companion object {
        fun hideSoftKeyboard(activity: Activity, binding: ViewBinding) {
            //Hide soft keyboard
            val imm = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }
    }

}