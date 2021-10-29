package com.ict311.task3

import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ItemUiFragmentBinding
import com.ict311.task3.utils.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * This fragment controller class controls the single activity view
 * Retrieving the activity information, letting the user create/update/delete and activity
 *
 * @author Federico Rusconi
 *
 */
class ItemUIFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: ItemUIViewModel
    private val args: ItemUIFragmentArgs by navArgs()
    private lateinit var binding: ItemUiFragmentBinding
    private lateinit var selectedActivity: ActivityEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ItemUIViewModel::class.java)
        selectedActivity = ActivityEntity(args.activityId)
        //Get selected activity
        viewModel.getActivityById(args.activityId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set support action bar
        setHomeButtonCheck()
        setHasOptionsMenu(true)

        //Change bar title
        requireActivity().title =
            if (args.activityId == NEW_ACTIVITY_ID) {
                getString(R.string.new_activity)
            } else {
                getString(R.string.edit_activity)
            }

        //Bind view elements
        binding = ItemUiFragmentBinding.inflate(inflater, container, false)
        //Handle back button
        handleBackPressed()
        //Observe selected activity and populate form
        observeActivity(savedInstanceState)

        return binding.root
    }

    /**
     *  Add Text Watchers to input texts
     *  Add listeners to UI widgets
     */
    override fun onStart() {
        super.onStart()
        //Title
        binding.activityTitle.addTextChangedListener(
            TextWatcher(binding.activityTitle)
        )
        //Place
        binding.activityPlace.addTextChangedListener(
            TextWatcher(binding.activityPlace)
        )
        //Date - Text watcher is needed to keep displaying selection on configuration changes
        //Display current date by default
        binding.activityDate.text = DateFormat.format(DATE_PRETTY, selectedActivity.date)
        binding.activityDate.addTextChangedListener(
            TextWatcher(binding.activityDate)
        )
        binding.activityDate.setOnClickListener {
            dateClicked()
        }
        //Group or Individual
        createArrayAdapter()
        binding.activityType.onItemSelectedListener = this
        //Start Time - Text watcher is needed to keep displaying selection on configuration changes
        binding.startTime.addTextChangedListener(
            TextWatcher(binding.startTime)
        )
        binding.startTime.setOnClickListener {
            timeClicked(selectedActivity.startTime.toFloat(), "start", DIALOG_TIME_START_KEY)
        }
        //End Time - Text watcher is needed to keep displaying selection on configuration changes
        binding.endTime.addTextChangedListener(
            TextWatcher(binding.endTime)
        )
        binding.endTime.setOnClickListener {
            timeClicked(selectedActivity.endTime.toFloat(), "end", DIALOG_TIME_END_KEY)
        }
    }

    /**
     * Create an array adapter to be used in the spinner widget
     * (Provide the user with a list of options to select from)
     */
    private fun createArrayAdapter() {
        ArrayAdapter.createFromResource(
            requireActivity(),
            R.array.activity_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.activityType.adapter = adapter
        }
    }

    /**
     * Date button has been clicked
     * Open Date picker dialog
     * Listen to date selected and set activity date
     */
    private fun dateClicked() {
        val action = ItemUIFragmentDirections.actionDatePicker(selectedActivity.date.time)
        val navController = findNavController()
        navController.navigate(action)
        //Observe selected date sent back from DatePickerFragment
        Helpers.getNavigationResult(
            navController,
            R.id.itemUIFragment,
            DIALOG_DATE_KEY
        ) { date: Date ->
            selectedActivity.date = date
            binding.activityDate.text = DateFormat.format(DATE_PRETTY, date)
        }
    }

    /**
     * Time button has been clicked
     * Open Time picker dialog
     * Listen to time selected and set activity start or end time
     * @param defaultTime The default value of the time picker
     * @param startOrEnd Pass "start" for startTime or "end" for endTime
     * @param dialogKey The name of the key to listen to (from constants.kt)
     */
    private fun timeClicked(defaultTime: Float, startOrEnd: String, dialogKey: String) {
        val action = ItemUIFragmentDirections.actionTimePicker(defaultTime, startOrEnd)
        val navController = findNavController()
        navController.navigate(action)
        //Observe selected time sent back from TimePickerFragment
        Helpers.getNavigationResult(
            navController,
            R.id.itemUIFragment,
            dialogKey
        ) { time: Double ->
            when (startOrEnd) {
                "start" -> {
                    selectedActivity.startTime = time
                    //Display to two decimal places
                    binding.startTime.text = String.format("%.2f", time)
                }
                "end" -> {
                    selectedActivity.endTime = time
                    //Display to two decimal places
                    binding.endTime.text = String.format("%.2f", time)
                }
            }

        }
    }

    /**
     * Inflate custom menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.item_ui_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Handle top menu clicks
     * @item The menu item that has been clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveActivity()
            R.id.deleteActivity -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.delete_activity_question),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    getString(R.string.confirm)
                ) {
                    deleteActivity()
                }.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Save activity information
     * to maintain state on configuration changes
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ACT_TITLE_KEY, selectedActivity.title)
        outState.putLong(ACT_DATE_KEY, selectedActivity.date.time)
        outState.putInt(ACT_TITLE_CURSOR_POS, binding.activityTitle.selectionStart)
        outState.putBoolean(ACT_TYPE_KEY, selectedActivity.isGroup)
        outState.putDouble(ACT_START_KEY, selectedActivity.startTime)
        outState.putDouble(ACT_END_KEY, selectedActivity.endTime)
        super.onSaveInstanceState(outState)
    }

    /**
     * When user selects activity type from spinner
     */
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        selectedActivity.isGroup = when (parent.getItemAtPosition(pos)) {
            "Group" -> true
            else -> false
        }
    }

    /**
     * When user does not select activity type from spinner
     */
    override fun onNothingSelected(parent: AdapterView<*>) {
        Toast.makeText(
            context,
            getString(R.string.please_select_type),
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * Retrieve and observe selected activity
     */
    private fun observeActivity(savedInstanceState: Bundle?) {
        viewModel.activityLiveData.observe(
            viewLifecycleOwner,
            { activity ->
                activity?.let {
                    selectedActivity = activity
                    //Title
                    val savedTitle = savedInstanceState?.getString(ACT_TITLE_KEY)
                    val savedTitleCursorPos = savedInstanceState?.getInt(ACT_TITLE_CURSOR_POS) ?: 0
                    binding.activityTitle.setText(savedTitle ?: it.title)
                    binding.activityTitle.setSelection(savedTitleCursorPos)
                    //Date
                    Log.i(LOG_TAG, selectedActivity.date.toString())
                    val savedDate = savedInstanceState?.getLong(ACT_DATE_KEY)
                    binding.activityDate.text =
                        if (savedDate !== null) {
                            DateFormat.format(DATE_PRETTY, savedDate)
                        } else {
                            DateFormat.format(DATE_PRETTY, it.date)
                        }

                    //Place
                    val savedPlace = savedInstanceState?.getString(ACT_PLACE_KEY)
                    val savedPlaceCursorPos = savedInstanceState?.getInt(ACT_PLACE_CURSOR_POS) ?: 0
                    binding.activityPlace.setText(savedPlace ?: it.place)
                    binding.activityPlace.setSelection(savedPlaceCursorPos)
                    //Group or individual
                    val savedIsGroup = savedInstanceState?.getBoolean(ACT_TYPE_KEY) ?: it.isGroup
                    //If null or false, set spinner to individual
                    if (!savedIsGroup) {
                        binding.activityType.setSelection(1)
                    }
                    //Start Time
                    val savedStart = savedInstanceState?.getDouble(ACT_START_KEY) ?: it.startTime
                    binding.startTime.text = String.format("%.2f", savedStart)
                    //End Time
                    val savedEnd = savedInstanceState?.getDouble(ACT_END_KEY) ?: it.endTime
                    binding.endTime.text = String.format("%.2f", savedEnd)
                    //Set clone in viewModel to detect changes
                    if (viewModel.activityChangeDetect == null) {
                        viewModel.activityChangeDetect = selectedActivity.clone()
                    }
                }
            }
        )
    }

    /**
     * Handle user pressing "Back" arrow
     * Moving this to onStop method would cause doubling data
     * when click "Check" top button
     */
    private fun handleBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    saveActivity()
                }
            }
        )
    }

    /**
     * Update activity in DB with value from form
     */
    private fun saveActivity(): Boolean {
        Helpers.hideSoftKeyboard(requireActivity(), binding)
        when (viewModel.saveUpdateActivity(selectedActivity)) {
            1 -> {
                //Display a message only if the activity has been modified
                if (selectedActivity.compareTo(viewModel.activityChangeDetect) != 0) {
                    Toast.makeText(
                        context,
                        R.string.activity_saved,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            -1 -> {
                Toast.makeText(
                    context,
                    getString(R.string.some_fields_missing),
                    Toast.LENGTH_SHORT
                ).show()
                return true
            }
        }
        //Back to previous view
        findNavController().navigateUp()
        //Reset activity change detector
        viewModel.activityChangeDetect = null
        return true
    }

    /**
     * Delete selected activity
     */
    private fun deleteActivity() {
        viewModel.deleteActivityById(selectedActivity)
        Helpers.hideSoftKeyboard(requireActivity(), binding)
        Toast.makeText(
            context,
            R.string.activity_deleted,
            Toast.LENGTH_SHORT
        ).show()
        //Reset activity change detector
        viewModel.activityChangeDetect = null
        //Back to previous view
        findNavController().navigateUp()
    }

    /**
     * Define and set "Check" button on top-left
     */
    private fun setHomeButtonCheck() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }
    }

    /**
     * Inner class used to define multiple text watchers for two-way data binding
     */
    private inner class TextWatcher(val view: View) : android.text.TextWatcher {

        override fun beforeTextChanged(
            sequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
            when (view.id) {
                R.id.activityTitle -> {
                    selectedActivity.title = sequence.toString()
                }
                R.id.activityPlace -> {
                    selectedActivity.place = sequence.toString()
                }
                R.id.activityDate -> {
                    val date =
                        SimpleDateFormat(DATE_PRETTY, Locale.ENGLISH).parse(sequence.toString())
                    date?.let {
                        selectedActivity.date = it
                    }
                }
                R.id.startTime -> {
                    selectedActivity.startTime = sequence.toString().toDouble()
                }
                R.id.endTime -> {
                    selectedActivity.endTime = sequence.toString().toDouble()
                }
            }
        }

        override fun afterTextChanged(sequence: Editable?) {}
    }

}