package com.example.blooms.mainApp.allMyGoal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.model.Goal


class AllMyGoalsAdapter(private val context: Context
, private val goals: Array<Goal>, private val onItemClick: (Int) -> Unit ) :
        RecyclerView.Adapter<AllMyGoalsAdapter.GridViewHolder>() {

        class GridViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//            val imageView: ImageView = view.findViewById(R.id.itemImage)
//            val textView: TextView = view.findViewById(R.id.itemText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_goal_item, parent, false)
            return GridViewHolder(view)
        }

        override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
            val item = goals[position]
            holder.itemView.setOnClickListener {
                onItemClick.invoke(position)
            }
//            holder.imageView.setImageResource(item.imageResId)
//            holder.textView.text = item.title
        }

        override fun getItemCount(): Int = goals.size
    }

