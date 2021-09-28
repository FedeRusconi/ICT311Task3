package com.ict311.task3

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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

        binding = ItemUiFragmentBinding.inflate(inflater, container, false)
        binding.activityTitle.setText(args.activityId.toString())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ItemUIViewModel::class.java)

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
        findNavController().navigateUp()

        return true
    }

}