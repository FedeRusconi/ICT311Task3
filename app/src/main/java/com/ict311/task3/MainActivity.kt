package com.ict311.task3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * This is the main (and only) activity of the app.
 * This activity serves as a container for reactively displaying different fragments
 *
 * @author Federico Rusconi
 *
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}