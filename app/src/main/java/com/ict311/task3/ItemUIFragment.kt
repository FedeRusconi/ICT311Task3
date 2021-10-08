package com.ict311.task3

import android.os.Bundle
import android.text.Editable
import android.text.format.DateFormat
import android.view.*
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.jumpDrawablesToCurrentState
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

class ItemUIFragment : Fragment() {

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
     *  Add listener to dialog buttons
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
        //Date
        binding.activityDate.addTextChangedListener(
            TextWatcher(binding.activityDate)
        )
        binding.activityDate.setOnClickListener {
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
        //Group or Individual
        binding.groupActivity.setOnCheckedChangeListener { _, isChecked ->
            selectedActivity.isGroup = isChecked
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
     * Save input texts and cursor position
     * to maintain state on configuration changes
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ACT_TITLE_KEY, selectedActivity.title)
        outState.putLong(ACT_DATE_KEY, selectedActivity.date.time)
        outState.putInt(ACT_TITLE_CURSOR_POS, binding.activityTitle.selectionStart)
        super.onSaveInstanceState(outState)
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
                    binding.groupActivity.apply {
                        isChecked = it.isGroup
                        jumpDrawablesToCurrentState()
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
        if (viewModel.saveUpdateActivity(selectedActivity)) {
            Toast.makeText(
                context,
                R.string.activity_saved,
                Toast.LENGTH_SHORT
            ).show()
            //Back to previous view
            findNavController().navigateUp()
        } else {
            Toast.makeText(
                context,
                R.string.error_title_empty,
                Toast.LENGTH_SHORT
            ).show()
        }
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
     * Inner class used to define multiple text watchers
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
            }
        }

        override fun afterTextChanged(sequence: Editable?) {}
    }

}