package com.example.blooms.mainApp.addNewGoal.goalStep
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R

class GoalStepAdapter(private val itemList: ArrayList<String>) : RecyclerView.Adapter<GoalStepAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.goal_step_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = itemList[position]
    }

    override fun getItemCount(): Int = itemList.size

    fun addItem(newItem: String) {
        itemList.add(newItem)
        notifyItemInserted(itemList.size - 1)
    }
}

