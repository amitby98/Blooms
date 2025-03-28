package com.example.blooms.allMyPosts.Lobby.AllMyPostsLobbyViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.allMyPosts.Lobby.allMyPostsLobbyRepository.AllMyPostsLobbyRepository
import com.example.blooms.model.EditPagePostWithGoalID
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import com.example.blooms.model.User
import kotlinx.coroutines.launch


class AllMyPostsLobbyViewModel : ViewModel() {
    private val repository = AllMyPostsLobbyRepository()

    private val _allMyPostsState = MutableLiveData<AllMyPostsLobbyState>()
    val allMyPostsState: LiveData<AllMyPostsLobbyState> = _allMyPostsState

    fun getAllMyPosts(user: User? = null) {
        viewModelScope.launch {
            repository.getAllMyPosts()
                .onSuccess { myGoals ->
                    if (myGoals.isNotEmpty()) {
                        // Collect all posts from all Goal objects and sort by postDateAndTime
                        val allPosts: List<Post> = myGoals.flatMap { it.posts }.sortedBy { it.postDateAndTime }
                        _allMyPostsState.value = AllMyPostsLobbyState.GetAllMyPostsSuccess(myGoals, user)
                    }
                }
                .onFailure { exception ->
                    _allMyPostsState.value = AllMyPostsLobbyState.GetAllMyPostsError(exception.message ?: "All My Goals Posts failed")
                }
        }
    }

    fun getAllRelevantPosts(goalsList : List<Goal>) : List<EditPagePostWithGoalID> {
        val postsList = arrayListOf<EditPagePostWithGoalID>()
        goalsList.forEach { goal ->
                goal.posts.forEach { post ->
                    val editPagePostWithGoalID = EditPagePostWithGoalID(
                        userName = goal.userName,
                        userProfileImage = goal.userImage,
                        post = post,
                        goalId = goal.goalId
                    )
                    postsList.add(editPagePostWithGoalID)
                }
        }
        return postsList.sortedByDescending { it.post.postDateAndTime }
    }

    fun getUserData() {
        _allMyPostsState.value = AllMyPostsLobbyState.Loading
        viewModelScope.launch {
            repository.getUserData()
                .onSuccess { user ->
                    user?.let {
                        //_allMyPostsState.value = AllMyPostsState.GetUserDataSuccess(user = user)
                        getAllMyPosts(user)
                    }.run {
                        getAllMyPosts()
                    }
                }
                .onFailure { exception ->
                    getAllMyPosts()
                }
        }
    }
}