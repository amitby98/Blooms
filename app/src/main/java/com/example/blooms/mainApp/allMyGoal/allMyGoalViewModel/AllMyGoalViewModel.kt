package com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.allMyGoal.allMyGoalRepository.AllMyGoalRepository
import com.example.blooms.mainApp.profile.profileViewModel.ProfileState
import kotlinx.coroutines.launch


class AllMyGoalViewModel : ViewModel() {
    private val repository = AllMyGoalRepository()

    private val _allMyGoalState = MutableLiveData<AllMyGoalState>()
    val allMyGoalState: LiveData<AllMyGoalState> = _allMyGoalState

    fun getAllMyGoals() {
        viewModelScope.launch {
            repository.getAllMyGoals()
                .onSuccess { myGoals ->
                        _allMyGoalState.value = AllMyGoalState.GetAllMyGoalsSuccess(myGoals)
                }
                .onFailure { exception ->
                    //TODO: need to take from ROOM
                    _allMyGoalState.value = AllMyGoalState.GetAllMyGoalsError(exception.message ?: "All My Goals Posts failed")
                }
        }
    }

    fun getUserData() {
        _allMyGoalState.value = AllMyGoalState.Loading
        viewModelScope.launch {
            repository.getUserData()
                .onSuccess { user ->
                    user?.let {
                        _allMyGoalState.value = AllMyGoalState.GetUserDataSuccess(user = user)
                        getAllMyGoals()
                    }.run {
                        getAllMyGoals()
                    }
                }
                .onFailure { exception ->
                    getAllMyGoals()
                }
        }
    }
}