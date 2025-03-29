package com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel

import com.example.blooms.model.Goal
import com.example.blooms.model.User

sealed class AllMyGoalState {
    data object Loading : AllMyGoalState()
    data class GetAllMyGoalsSuccess(val goals: List<Goal>) : AllMyGoalState()
    data class GetUserDataSuccess(val user: User) : AllMyGoalState()
    data class GetAllMyGoalsError(val message: String) : AllMyGoalState()
    data object DeleteGoalSuccess : AllMyGoalState()
    data class DeleteGoalError(val message: String) : AllMyGoalState()
}