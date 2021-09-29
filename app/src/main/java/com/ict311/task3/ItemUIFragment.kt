package com.ict311.task3

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ict311.task3.databinding.ItemUiFragmentBinding

class ItemUIFragment : Fragment() {

    private lateinit var viewModel: ItemUIViewModel
    private val args: ItemUIFragmentArgs by navArgs()
    private lateinit var binding: ItemUiFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_check)
        }

        viewModel = ViewModelProvider(this).get(ItemUIViewModel::class.java)

        binding = ItemUiFragmentBinding.inflate(inflater, container, false)
        binding.activityTitle.setText("")

        //Observe selected activity and populate form
        viewModel.currentActivity.observe(viewLifecycleOwner, Observer {
            binding.activityTitle.setText(it.title)
        })
        //Get selected activity
        viewModel.getActivityById(args.activityId)

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> saveActivity()
            else -> super.onOptionsItemSelected(item)
        }
    }

    /* SEE IF NEEDED TO ADD DELETE BUTTON
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.item_ui_fragment, menu)
    }*/

    override fun onStop() {
        super.onStop()
        saveActivity()
    }

    private fun saveActivity(): Boolean {
        //Hide soft keyboard
        val imm = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
        //Save or update activity
        viewModel.currentActivity.value?.title = binding.activityTitle.text.toString()
        if(viewModel.saveUpdateActivity()) {
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