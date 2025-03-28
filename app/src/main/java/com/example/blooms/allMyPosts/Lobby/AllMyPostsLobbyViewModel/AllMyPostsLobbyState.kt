package com.example.blooms.allMyPosts.Lobby.AllMyPostsLobbyViewModel

import com.example.blooms.model.Goal
import com.example.blooms.model.User

sealed class AllMyPostsLobbyState {
    data object Loading : AllMyPostsLobbyState()
    data class GetAllMyPostsSuccess(val goals: List<Goal>, val user: User?) : AllMyPostsLobbyState()
    data class GetUserDataSuccess(val user: User) : AllMyPostsLobbyState()
    data class GetAllMyPostsError(val message: String) : AllMyPostsLobbyState()
}