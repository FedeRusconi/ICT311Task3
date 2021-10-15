package com.ict311.task3.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding

class Helpers {

    companion object {
        /**
         * Check if ID is the one set for new activities
         * @param id The activity
         * @return True if it's new activity
         */
        fun isNewActivity(id: Int): Boolean {
            return id == NEW_ACTIVITY_ID
        }

        /**
         * Hide the device soft keyboard
         * @param activity The current activity
         * @param binding The object which contains view references
         */
        fun hideSoftKeyboard(activity: Activity, binding: ViewBinding) {
            //Hide soft keyboard
            val imm = activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }

        /**
         * Set result of navigation action
         * @param navController Navigation Controller
         * @param key The key to set
         * @param value The value to pair
         */
        fun <T> setNavigationResult(navController: NavController, key: String, value: T) {
            navController.previousBackStackEntry?.savedStateHandle?.set(
                key,
                value
            )
        }

        /**
         * Retrieve result of navigation action
         * @param navController Navigation Controller
         * @param id The resource ID reference
         * @param key The key to retrieve
         * @param onResult Callback method that is triggered once the result is retrieved
         */
        fun <T> getNavigationResult(
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