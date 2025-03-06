package com.example.blooms.mainApp.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.ImageUtils
import com.example.blooms.model.HomePagePosts
import com.example.blooms.model.Post
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class AllPostsAdapter(private val allPosts: List<HomePagePosts>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<AllPostsAdapter.AllPostsViewHolder>() {

    inner class AllPostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageProfile: AppCompatImageView = view.findViewById(R.id.profile_image)
        val title: AppCompatTextView = view.findViewById(R.id.post_title)
        val message: AppCompatTextView = view.findViewById(R.id.post_message)
        val userName: AppCompatTextView = view.findViewById(R.id.user_name)
        val createDate: AppCompatTextView = view.findViewById(R.id.create_date_post)
        val postImage: AppCompatImageView = view.findViewById(R.id.post_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return AllPostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AllPostsViewHolder, position: Int) {
        val homePagePost = allPosts[position]
        holder.userName.text = homePagePost.userName
        holder.title.text = homePagePost.post.title
        holder.message.text = homePagePost.post.message
        val currentTimeMillis = homePagePost.post.postDateAndTime
        // Convert current time millis to an Instant
        val instant = Instant.ofEpochMilli(currentTimeMillis)

        // Define a formatter for the desired format
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault()) // Use the system default time zone

        // Format the Instant to the desired string format
        val formattedDate = formatter.format(instant)
        holder.createDate.text = formattedDate
        holder.imageProfile.setImageBitmap(ImageUtils.convertBase64ToBitmap(homePagePost.userProfileImage))
        holder.postImage.setImageBitmap(ImageUtils.convertBase64ToBitmap(homePagePost.post.image))

        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    override fun getItemCount() = allPosts.size
}
