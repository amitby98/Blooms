package com.example.blooms.mainApp.addNewGoal.addGoalViewModel

sealed class AddGoalState {
    object Loading : AddGoalState()
    object UploadPostSuccess : AddGoalState()
    data class UploadPostError(val message: String) : AddGoalState()
}