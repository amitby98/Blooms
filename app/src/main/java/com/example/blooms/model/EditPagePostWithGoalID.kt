package com.example.blooms.model

data class EditPagePostWithGoalID(
    val userName: String = "",
    val userProfileImage: String = "",
    val post: Post = Post(),
    val goalId: String = ""
)
