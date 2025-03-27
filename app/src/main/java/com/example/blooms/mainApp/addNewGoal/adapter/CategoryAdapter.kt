package com.example.blooms.mainApp.addNewGoal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.model.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryCard: CardView = itemView.findViewById(R.id.category_card)
        val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
        val categoryName: TextView = itemView.findViewById(R.id.category_name)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]

        // Set icon
        holder.categoryIcon.setImageResource(category.icon)

        // Set name
        holder.categoryName.text = category.name

        // Update selection state
        if (category.isSelected) {
            holder.categoryCard.setCardBackgroundColor(holder.itemView.context.getColor(R.color.colorPrimary))
            holder.categoryName.setTextColor(holder.itemView.context.getColor(R.color.white))
            holder.categoryIcon.setColorFilter(holder.itemView.context.getColor(R.color.white))

            // Add scale animation
            holder.categoryCard.animate().scaleX(1.05f).scaleY(1.05f).setDuration(200).start()
        } else {
            holder.categoryCard.setCardBackgroundColor(holder.itemView.context.getColor(R.color.white))
            holder.categoryName.setTextColor(holder.itemView.context.getColor(R.color.black))
            holder.categoryIcon.setColorFilter(holder.itemView.context.getColor(R.color.colorPrimary))

            // Reset scale
            holder.categoryCard.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
        }
    }

    override fun getItemCount(): Int = categories.size
}