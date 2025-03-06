package com.example.blooms.mainApp.addNewGoal.goalStep

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.Constance
import com.example.blooms.model.GoalStep


class GoalStepAdapter(private val context: Context, private val itemList: ArrayList<GoalStep>) :
    RecyclerView.Adapter<GoalStepAdapter.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION  // Track selected row

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mItemText: AppCompatTextView = itemView.findViewById(R.id.item_goal_text)
        val mItemCheckbox: AppCompatCheckBox = itemView.findViewById(R.id.item_goal_checkbox)
        val mImage: AppCompatImageView = itemView.findViewById(R.id.image)
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
        holder.mItemCheckbox.isEnabled = position == selectedPosition || selectedPosition == RecyclerView.NO_POSITION
        var drawable = ContextCompat.getDrawable(context, com.example.blooms.R.drawable.ic_target)
        Constance.targets.get(position)?.let {
            drawable = ContextCompat.getDrawable(context, it)
        }
        holder.mImage.setImageDrawable(drawable)
        holder.mItemText.text = goalStep.text
        // Handle checkbox clicks
        holder.mItemCheckbox.setOnClickListener {
            if (holder.mItemCheckbox.isChecked) {
                // Uncheck all other items and set selectedPosition
                for (i in itemList.indices) {
                    itemList[i].isChecked = i == position
                }
                selectedPosition = position
            } else {
                // If unchecked, reset selection and enable all checkboxes
                itemList[position].isChecked = false
                selectedPosition = RecyclerView.NO_POSITION
            }
            notifyDataSetChanged() // Refresh UI
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun addItem(newItem: String) {
        itemList.add(GoalStep(newItem, false))
        notifyItemInserted(itemList.size - 1)
    }
}
