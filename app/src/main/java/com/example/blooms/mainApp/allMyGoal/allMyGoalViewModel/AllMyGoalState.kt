package com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel

import com.example.blooms.model.Goal

sealed class AllMyGoalState {
    object Loading : AllMyGoalState()
    data class GetAllMyGoalsSuccess(val goals: List<Goal>) : AllMyGoalState()
    data class GetAllMyGoalsError(val message: String) : AllMyGoalState()
}