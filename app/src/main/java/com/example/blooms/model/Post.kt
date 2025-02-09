package com.example.blooms.model

data class Post(
    val userId: String = "",
    val category: String = "",
    val title: String = "",
    val body: String = "",
    val postDateAndTime: String = "",
    val finishDate: String = "",
    val postImage: String = ""
)