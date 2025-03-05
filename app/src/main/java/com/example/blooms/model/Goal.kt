package com.example.blooms.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goals")
data class Goal (
    @PrimaryKey var goalId: String = "",
    val title: String = "",
    val userId: String = "",
    val categoryId: Int = -1,
    val deadlineDate: String = "",
    val shareGoal: Boolean = true,
    val posts: ArrayList<Post> = arrayListOf(),
    val goalStep: ArrayList<GoalStep> = arrayListOf(),
    var userImage: String = "",
    var userName: String = ""

) : Serializable