package com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.allMyGoal.allMyGoalRepository.AllMyGoalRepository
import kotlinx.coroutines.launch


class AllMyGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AllMyGoalRepository(application)

    private val _allMyGoalState = MutableLiveData<AllMyGoalState>()
    val allMyGoalState: LiveData<AllMyGoalState> = _allMyGoalState

    fun getAllMyGoals() {
        _allMyGoalState.value = AllMyGoalState.Loading
        viewModelScope.launch {
            repository.getAllMyPosts()
                .onSuccess { posts ->
                    if (posts.isNotEmpty()) {
                        _allMyGoalState.value = AllMyGoalState.GetAllMyPostSuccess(posts)
                    } else {

                    }
                }
                .onFailure { exception ->
                    //TODO: need to take from ROOM
                    _allMyGoalState.value = AllMyGoalState.GetAllMyPostError(exception.message ?: "All My Goals Posts failed")
                }
        }
    }
}