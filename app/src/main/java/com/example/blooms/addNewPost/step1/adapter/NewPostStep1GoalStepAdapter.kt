package com.example.blooms.addNewPost.step1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.model.GoalStep

class NewPostStep1GoalStepAdapter(private val itemList: ArrayList<GoalStep>,
    private val onDateSelected: (GoalStep,Int) -> Unit ) :
    RecyclerView.Adapter<NewPostStep1GoalStepAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mItemText: AppCompatTextView = itemView.findViewById(R.id.item_goal_text)
        val mItemCheckbox: AppCompatCheckBox = itemView.findViewById(R.id.item_goal_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.goal_step_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goalStep = itemList[position]

        // Set checkbox state and enable/disable checkboxes based on selection
        holder.mItemCheckbox.isChecked = goalStep.isChecked
        holder.mItemCheckbox.isEnabled = !goalStep.isChecked

        holder.mItemText.text = goalStep.text
        holder.mItemCheckbox.setOnClickListener {
            onDateSelected.invoke(goalStep, position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun addItem(newItem: String) {
        itemList.add(GoalStep(newItem, false))
        notifyItemInserted(itemList.size - 1)
    }
}
