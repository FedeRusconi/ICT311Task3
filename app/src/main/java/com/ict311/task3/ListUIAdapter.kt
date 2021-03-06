package com.ict311.task3

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ListItemBinding
import com.ict311.task3.utils.DATE_PRETTY

/**
 * This class is an adapter class used to generate multiple list items
 * for displaying a list of user's activities
 *
 * @author Federico Rusconi
 *
 */
class ListUIAdapter(
    private val activitiesList: List<ActivityEntity>,
    private val callbacks: Callbacks
) :
    RecyclerView.Adapter<ListUIAdapter.ViewHolder>() {

    val selectedActivities = arrayListOf<ActivityEntity>()

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val binding = ListItemBinding.bind(itemView)
        private lateinit var activity: ActivityEntity

        init {
            binding.root.setOnClickListener(this)
        }

        /**
         * Bind vew to activity information
         * @param activity The selected activity
         */
        fun bind(activity: ActivityEntity) {
            this.activity = activity
            binding.activityTitle.text = activity.title
            binding.activityDate.text = DateFormat.format(DATE_PRETTY, activity.date)
            binding.activityPlace.text = activity.place
            //Select/De-select activity
            binding.floatingActionButton.setOnClickListener {
                if (selectedActivities.contains(activity)) {
                    selectedActivities.remove(activity)
                    binding.floatingActionButton.setImageResource(R.drawable.ic_fitness)
                } else {
                    selectedActivities.add(activity)
                    binding.floatingActionButton.setImageResource(R.drawable.ic_check)
                }
                //Notify fragment to change menu bar
                callbacks.onItemSelectionChanged()
            }
            //Set selected activities on scrolling
            binding.floatingActionButton.setImageResource(
                if (selectedActivities.contains(activity)) {
                    R.drawable.ic_check
                } else {
                    R.drawable.ic_fitness
                }
            )
        }

        /**
         * Activity list item has been clicked
         */
        override fun onClick(v: View?) {
            callbacks.onItemCLicked(activity.id)
        }
    }

    /**
     * Triggered when view holder is created
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    /**
     * Triggered when view holder binds the data
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activitiesList[position]
        holder.bind(activity)
    }

    /**
     * Returns the number of activities in list
     */
    override fun getItemCount() = activitiesList.size

    /**
     * Interface defined to notify parent fragment of events happened
     */
    interface Callbacks {
        fun onItemCLicked(activityId: Int)
        fun onItemSelectionChanged()
    }
}