package com.example.blooms.addNewPost.step2.addNewPostStep2ViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.addNewPost.step2.addNewPostStep2Repository.AddNewPostStep2Repository
import com.example.blooms.mainApp.addNewGoal.addGoalRepository.AddGoalRepository
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import kotlinx.coroutines.launch


class AddNewPostStep2ViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AddNewPostStep2Repository(application)

    private val _addNewPostStep2State = MutableLiveData<AddNewPostStep2State>()
    val addNewPostStep2State: LiveData<AddNewPostStep2State> = _addNewPostStep2State

    fun uploadPost(goal: Goal, postPosition: Int) {
        _addNewPostStep2State.value = AddNewPostStep2State.Loading
        viewModelScope.launch {
            repository.updateGoal(goal,postPosition)
                .onSuccess {
                    _addNewPostStep2State.value = AddNewPostStep2State.UpdatePostSuccess
                }
                .onFailure { exception ->
                    _addNewPostStep2State.value = AddNewPostStep2State.UpdatePostError(exception.message ?: "Update failed")
                }
        }
    }
}