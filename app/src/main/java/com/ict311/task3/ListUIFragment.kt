package com.ict311.task3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ListUiFragmentBinding
import java.util.*

class ListUIFragment : Fragment(), ListUIAdapter.Callbacks {

    private lateinit var viewModel: ListUIViewModel
    private lateinit var binding: ListUiFragmentBinding
    private lateinit var adapter: ListUIAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding = ListUiFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ListUIViewModel::class.java)

        //Add a divider between list items
        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.activitiesList?.observe(viewLifecycleOwner, Observer {
            adapter = ListUIAdapter(it, this@ListUIFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onItemCLicked(activityId: UUID) {
        val action = ListUIFragmentDirections.actionItemUi(activityId)
        findNavController().navigate(action)
    }

    override fun onItemSelectionChanged() {
        //Reset options menu to toggle between two menus
        requireActivity().invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if (this::adapter.isInitialized && adapter.selectedActivities.isNotEmpty()) {
                R.menu.list_ui_fragment_selected_items
            } else {
                R.menu.list_ui_fragment
            }
        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newActivity -> addNewActivity()
            R.id.deleteActivities -> deleteSelectedActivities()
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun addNewActivity() : Boolean {
        //This causes issue + access database in main thread issue
        onItemCLicked(UUID.fromString(NEW_ACTIVITY_ID))
        return true
    }

    private fun deleteSelectedActivities(): Boolean {
        viewModel.deleteActivities(adapter.selectedActivities)
        //After deleting items, de-select items
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedActivities.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
        return true
    }

    /* MIGHT USE THIS FOR CLARITY
    private fun updateUI(activities: List<ActivityEntity>) {
        adapter = ListUIAdapter(activities)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }*/

}