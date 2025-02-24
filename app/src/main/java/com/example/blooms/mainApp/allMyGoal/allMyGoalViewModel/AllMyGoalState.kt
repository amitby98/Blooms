package com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel

import com.example.blooms.model.Post

sealed class AllMyGoalState {
    object Loading : AllMyGoalState()
    data class GetAllMyPostSuccess(val posts: List<Post>) : AllMyGoalState()
    data class GetAllMyPostError(val message: String) : AllMyGoalState()
}