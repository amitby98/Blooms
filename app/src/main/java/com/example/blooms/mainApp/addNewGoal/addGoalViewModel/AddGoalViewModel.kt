package com.example.blooms.mainApp.addNewGoal.addGoalViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.addNewGoal.addGoalRepository.AddGoalRepository
import com.example.blooms.model.Post
import kotlinx.coroutines.launch


class AddGoalViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AddGoalRepository(application)

    private val _addGoalState = MutableLiveData<AddGoalState>()
    val addGoalState: LiveData<AddGoalState> = _addGoalState

    fun uploadPost(post: Post) {
        viewModelScope.launch {
            repository.uploadPost(post)
                .onSuccess {
                    _addGoalState.value = AddGoalState.UploadPostSuccess
                }
                .onFailure { exception ->
                    _addGoalState.value = AddGoalState.UploadPostError(exception.message ?: "Sign in failed")
                }
        }
    }
}