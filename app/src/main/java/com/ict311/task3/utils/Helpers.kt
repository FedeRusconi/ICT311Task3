package com.ict311.task3.utils

import android.app.Activity
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import java.util.*

class Helpers {

    companion object {
        fun hideSoftKeyboard(activity: Activity, binding: ViewBinding) {
            //Hide soft keyboard
            val imm = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        fun <T>setNavigationResult(navController: NavController, key: String, value: T) {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                key,
                value
            )
        }

        fun <T>getNavigationResult(
            navController: NavController,
            @IdRes id: Int,
            key: String,
            onResult: (result: T) -> Unit
        ) {
            val navBackStackEntry = navController.getBackStackEntry(id)

            val observer = LifecycleEventObserver { _, event ->

                if (event == Lifecycle.Event.ON_RESUME
                    && navBackStackEntry.savedStateHandle.contains(key)
                ) {
                    val result = navBackStackEntry.savedStateHandle.get<T>(key)
                    result?.let(onResult)
                    navBackStackEntry.savedStateHandle.remove<T>(key)
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)

            navBackStackEntry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

}