package com.ict311.task3

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ItemUiFragmentBinding
import com.ict311.task3.helpers.LOG_TAG
import com.ict311.task3.helpers.TextWatcher

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
    ): View? {

        //Set support action bar
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }
        setHasOptionsMenu(true)

        binding = ItemUiFragmentBinding.inflate(inflater, container, false)
        //binding.activityTitle.setText("")
        binding.activityTitle.addTextChangedListener(
            TextWatcher(binding.activityTitle, selectedActivity)
        )

        //Observe selected activity and populate form
        /*viewModel.currentActivity.observe(viewLifecycleOwner, Observer {
            binding.activityTitle.setText(it.title)
        })*/
        viewModel.activityLiveData.observe(
            viewLifecycleOwner,
            { activity ->
                activity?.let {
                    //updateUI()
                    selectedActivity = activity
                    binding.activityTitle.setText(it.title)
                }
            }
        )

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveActivity()
            else -> super.onOptionsItemSelected(item)
        }
    }

     //SEE IF NEEDED TO ADD DELETE BUTTON
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
         inflater.inflate(R.menu.item_ui_fragment, menu)
         super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStop() {
        super.onStop()
        saveActivity()
    }

    private fun saveActivity(): Boolean {
        Log.i(LOG_TAG, "saveActivity called")
        Log.i(LOG_TAG, selectedActivity.title)
        //Hide soft keyboard
        val imm = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        //Save or update activity
        if(viewModel.saveUpdateActivity(selectedActivity)) {
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

}