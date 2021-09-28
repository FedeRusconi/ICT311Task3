package com.ict311.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding = ListUiFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ListUIViewModel::class.java)

        with(binding.recyclerView) {
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
            addItemDecoration(divider)
        }

        viewModel.activitiesList.observe(viewLifecycleOwner, Observer {
            adapter = ListUIAdapter(it, this@ListUIFragment)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        })

        return binding.root
    }

    override fun onItemCLicked(activityId: UUID) {
        val action = ListUIFragmentDirections.actionItemUi(activityId)
        findNavController().navigate(action)
    }

    /* MIGHT USE THIS FOR CLARITY
    private fun updateUI(activities: List<ActivityEntity>) {
        adapter = ListUIAdapter(activities)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }*/

}