package com.example.blooms.model

data class Post(
    val userId: String = "",
    val categoryId: Int = -1,
    val title: String = "",
    val message: String = "",
    val postDateAndTime: String = "",
    val deadlineDate: String = "",
)