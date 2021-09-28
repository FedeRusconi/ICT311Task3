package com.ict311.task3

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ict311.task3.data.ActivityEntity
import com.ict311.task3.databinding.ListItemBinding
import java.util.*

class ListUIAdapter(private val activitiesList: List<ActivityEntity>,
                    private val callbacks: Callbacks):
    RecyclerView.Adapter<ListUIAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View):
        RecyclerView.ViewHolder(itemView), View.OnClickListener{

        private val binding = ListItemBinding.bind(itemView)
        private lateinit var activity: ActivityEntity

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(activity: ActivityEntity) {
            this.activity = activity
            binding.activityTitle.text = activity.title
            binding.activityDate.text = DateFormat.format( "EEEE, MMM d, yyyy", activity.date)
            binding.activityPlace.text = activity.place
            /*solvedImageView.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }*/
        }

        override fun onClick(v: View?) {
            callbacks.onItemCLicked(activity.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activitiesList[position]
        holder.bind(activity)
        /*with(holder.binding) {
            activityTitle.text = activity.title
            activityDate.text = DateFormat.format( "EEEE, MMM d, yyyy", activity.date)
            activityPlace.text = activity.place

            root.setOnClickListener {
                callbacks.onItemCLicked(activity.id)
            }
        }*/
    }

    override fun getItemCount() = activitiesList.size

    interface Callbacks {
        fun onItemCLicked(activityId: UUID)
    }
}