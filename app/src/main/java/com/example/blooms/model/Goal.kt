package com.example.blooms.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal (
    @PrimaryKey var goalId: String = "",
    val title: String = "",
    val userId: String = "",
    val categoryId: Int = -1,
    val deadlineDate: String = "",
    val posts: ArrayList<Post> = arrayListOf(),
    val goalStep: ArrayList<GoalStep> = arrayListOf()

)