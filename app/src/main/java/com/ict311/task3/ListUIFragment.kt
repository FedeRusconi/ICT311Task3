package com.ict311.task3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ict311.task3.databinding.ListUiFragmentBinding

class ListUIFragment : Fragment() {

    private lateinit var viewModel: ListUIViewModel
    private lateinit var binding: ListUiFragmentBinding

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

        return binding.root
    }

}