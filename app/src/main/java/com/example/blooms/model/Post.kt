package com.example.blooms.model

import androidx.room.PrimaryKey
import java.io.Serializable

data class Post(
    @PrimaryKey var postId: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val postDateAndTime: Long = System.currentTimeMillis(),
    val image: String = ""
): Serializable