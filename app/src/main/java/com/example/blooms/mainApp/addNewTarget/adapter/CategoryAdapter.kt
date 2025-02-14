package com.example.blooms.mainApp.addNewTarget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.model.Category

class CategoryAdapter(private val categories: List<Category>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val background: AppCompatImageView = view.findViewById(R.id.background_selector)
        val imgCategory: AppCompatImageView = view.findViewById(R.id.imgCategory)
        val txtCategory: AppCompatTextView = view.findViewById(R.id.txtCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_post_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.imgCategory.setImageResource(category.icon)
        holder.txtCategory.text = category.name

        // Update background based on selection
        holder.background.isSelected = category.isSelected


        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = categories.size
}
