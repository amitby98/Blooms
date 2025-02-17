package com.example.blooms.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class Post(
    @PrimaryKey var postId: String = "",
    val userId: String = "",
    val categoryId: Int = -1,
    val title: String = "",
    val message: String = "",
    val postDateAndTime: Long = System.currentTimeMillis(),
    val deadlineDate: String = "",
)