package com.example.blooms.mainApp.addNewGoal.addGoalViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.addNewGoal.addGoalRepository.AddGoalRepository
import com.example.blooms.model.Goal
import com.example.blooms.model.User
import kotlinx.coroutines.launch


class AddGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AddGoalRepository(application)

    private val _addGoalState = MutableLiveData<AddGoalState>()
    val addGoalState: LiveData<AddGoalState> = _addGoalState

    fun addNewGoal(goal: Goal) {
        viewModelScope.launch {
            repository.uploadGoal(goal)
                .onSuccess {
                    _addGoalState.value = AddGoalState.UploadPostSuccess
                }
                .onFailure { exception ->
                    _addGoalState.value = AddGoalState.UploadPostError(exception.message ?: "Sign in failed")
                }
        }
    }


    fun getUserData(goal: Goal) {
        viewModelScope.launch {
            repository.getUserData()
                .onSuccess { user ->
                    user?.let {
                        var goalUpdate  = updateGoalWithUserData(goal, user)
                        addNewGoal(goalUpdate)
                    }.run {
                        addNewGoal(goal)
                    }
                }
                .onFailure { exception ->
                    addNewGoal(goal)
                }
        }
    }

    private fun updateGoalWithUserData(goal: Goal, userData: User): Goal {
        goal.userImage = userData.profileImage
        goal.userName = userData.firstName + " " + userData.lastName
        return goal
    }


}