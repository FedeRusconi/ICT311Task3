package com.ict311.task3

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ListUiFragmentBinding
import com.ict311.task3.utils.NEW_ACTIVITY_ID
import com.ict311.task3.utils.SELECTED_ACTIVITIES_KEY
import java.util.*

class ListUIFragment : Fragment(), ListUIAdapter.Callbacks {

    private lateinit var viewModel: ListUIViewModel
    private lateinit var binding: ListUiFragmentBinding
    private lateinit var adapter: ListUIAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Hide home button (needed only in ItemUI)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        //Reset bar title
        requireActivity().title = getString(R.string.app_name)

        //Bind view elements
        binding = ListUiFragmentBinding.inflate(inflater, container, false)
        //Add a divider between list items
        addListItemDivider()

        //Register viewModel
        viewModel = ViewModelProvider(this).get(ListUIViewModel::class.java)

        //Observe list
        observeList(savedInstanceState)

        return binding.root
    }

    /**
     * Navigate to itemUI and pass selected activity id
     * @param activityId The selected activity unique ID
     */
    override fun onItemCLicked(activityId: Int) {
        val action = ListUIFragmentDirections.actionItemUi(activityId)
        findNavController().navigate(action)
    }

    /**
     * Reset options menu to toggle between two menus
     */
    override fun onItemSelectionChanged() {
        requireActivity().invalidateOptionsMenu()
    }

    /**
     * Define and inflate which top menu to show
     */
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

    /**
     * Handle top menu items clicks
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.newActivity -> addNewActivity()
            R.id.deleteActivities -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.delete_activities_question),
                    Snackbar.LENGTH_LONG
                ).setAction(
                    getString(R.string.confirm)
                ) {
                    deleteSelectedActivities()
                }.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    /**
     * Save list of selected activity
     * to persist on configuration changes
     */
    override fun onSaveInstanceState(outState: Bundle) {
        if (this::adapter.isInitialized) {
            outState.putParcelableArrayList(
                SELECTED_ACTIVITIES_KEY,
                adapter.selectedActivities
            )
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Retrieve and observe the list of activities
     */
    private fun observeList(savedInstanceState: Bundle?) {
        viewModel.activitiesList.observe(viewLifecycleOwner, {
            adapter = ListUIAdapter(it, this@ListUIFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
            val selectedActivities =
                savedInstanceState?.getParcelableArrayList<ActivityEntity>(
                    SELECTED_ACTIVITIES_KEY
                )
            adapter.selectedActivities.addAll(selectedActivities ?: emptyList())
            //Display recycler view only if there are items
            if (adapter.itemCount > 0) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.notFoundContainer.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.notFoundContainer.visibility = View.VISIBLE
            }
        })
    }

    /**
     * Add new activity by passing pre-set ID
     */
    private fun addNewActivity(): Boolean {
        onItemCLicked(NEW_ACTIVITY_ID)
        return true
    }

    /**
     * Delete all selected activities
     * and clear selectedActivities list
     */
    private fun deleteSelectedActivities() {
        viewModel.deleteActivities(adapter.selectedActivities)
        //After deleting items, de-select items
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.selectedActivities.clear()
            requireActivity().invalidateOptionsMenu()
        }, 100)
    }

    /**
     * Add a divider between list items
     */
    private fun addListItemDivider() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }
    }

}