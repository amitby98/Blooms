package com.example.blooms.mainApp.allMyGoal.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.model.Post
import com.google.firebase.auth.FirebaseAuth

class AllMyGoalAdapter(private val myGoals: List<Post>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AllMyGoalAdapter.AllMyGoalViewHolder>() {

    inner class AllMyGoalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: AppCompatImageView = view.findViewById(R.id.profile_image)
        val title: AppCompatTextView = view.findViewById(R.id.post_title)
        val message: AppCompatTextView = view.findViewById(R.id.post_message)
        val userName: AppCompatTextView = view.findViewById(R.id.user_name)
        val createDate: AppCompatTextView = view.findViewById(R.id.create_date_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMyGoalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return AllMyGoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllMyGoalViewHolder, position: Int) {
        val myGoal = myGoals[position]
        holder.userName.text = FirebaseAuth.getInstance().currentUser?.uid ?:""
        holder.title.text = myGoal.title
        holder.message.text = myGoal.message
        holder.createDate.text = myGoal.postDateAndTime.toString()

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = myGoals.size
}
