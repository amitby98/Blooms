package com.example.blooms.allMyPosts.Lobby.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.ImageUtils
import com.example.blooms.model.EditPagePostWithGoalID
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

class AllMyPostsLobbyAdapter(private val allPosts: List<EditPagePostWithGoalID>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AllMyPostsLobbyAdapter.AllPostsViewHolder>() {

    inner class AllPostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: AppCompatImageView = view.findViewById(R.id.profile_image)
        val title: AppCompatTextView = view.findViewById(R.id.post_title)
        val message: AppCompatTextView = view.findViewById(R.id.post_message)
        val userName: AppCompatTextView = view.findViewById(R.id.user_name)
        val createDate: AppCompatTextView = view.findViewById(R.id.create_date_post)
        val postImage: AppCompatImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_my_post_item, parent, false)
        return AllPostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllPostsViewHolder, position: Int) {
        val post = allPosts[position]
        holder.userName.text = toTitleCase(post.userName)
        holder.title.text = post.post.title
        holder.message.text = post.post.message

        val currentTimeMillis = post.post.postDateAndTime
        holder.createDate.text = formatTimeAgo(currentTimeMillis)

        holder.imageProfile.setImageBitmap(ImageUtils.convertBase64ToBitmap(post.userProfileImage))
        if(!post.post.image.isNullOrEmpty()) {
            holder.postImage.setImageBitmap(ImageUtils.convertBase64ToBitmap(post.post.image))
        } else {
            holder.postImage.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    private fun toTitleCase(text: String): String {
        if (text.isEmpty()) return text

        return text.split(" ").joinToString(" ") { word ->
            if (word.isEmpty()) word
            else word.substring(0, 1).uppercase() + word.substring(1).lowercase()
        }
    }

    private fun formatTimeAgo(timestamp: Long): String {
        val now = Instant.now()
        val postTime = Instant.ofEpochMilli(timestamp)

        val secondsDiff = ChronoUnit.SECONDS.between(postTime, now)
        val minutesDiff = ChronoUnit.MINUTES.between(postTime, now)
        val hoursDiff = ChronoUnit.HOURS.between(postTime, now)

        return when {
            secondsDiff < 60 -> "just now"
            minutesDiff < 60 -> "$minutesDiff ${if (minutesDiff == 1L) "minute" else "minutes"} ago"
            hoursDiff < 24 -> "$hoursDiff ${if (hoursDiff == 1L) "hour" else "hours"} ago"
            else -> {
                val formatter = DateTimeFormatter
                    .ofPattern("MMMM d, yyyy", Locale.ENGLISH)
                    .withZone(ZoneId.systemDefault())
                formatter.format(postTime)
            }
        }
    }

    override fun getItemCount() = allPosts.size
}