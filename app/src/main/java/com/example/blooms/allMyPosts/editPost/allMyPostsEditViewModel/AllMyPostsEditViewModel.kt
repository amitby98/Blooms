package com.example.blooms.allMyPosts.editPost.allMyPostsEditViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.addNewPost.step2.addNewPostStep2Repository.AddNewPostStep2Repository
import com.example.blooms.allMyPosts.editPost.allMyPostEditRepository.AllMyPostsEditRepository
import com.example.blooms.mainApp.addNewGoal.addGoalRepository.AddGoalRepository
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import kotlinx.coroutines.launch


class AllMyPostsEditViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AllMyPostsEditRepository(application)

    private val _allMyPostsEditPostState = MutableLiveData<AllMyPostsEditState>()
    val allMyPostsEditPostState: LiveData<AllMyPostsEditState> = _allMyPostsEditPostState

    fun uploadPost(goal: Goal) {
        _allMyPostsEditPostState.value = AllMyPostsEditState.Loading
        viewModelScope.launch {
            repository.updateGoal(goal)
                .onSuccess {
                    _allMyPostsEditPostState.value = AllMyPostsEditState.UpdatePostSuccess
                }
                .onFailure { exception ->
                    _allMyPostsEditPostState.value = AllMyPostsEditState.UpdatePostError(exception.message ?: "Update failed")
                }
        }
    }
}