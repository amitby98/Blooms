package com.example.blooms.mainApp.home.homeViewModel

import com.example.blooms.model.Post

sealed class HomeState {
    object Loading : HomeState()
    data class GetAllPostSuccess(val posts: List<Post>) : HomeState()
    data class GetAllPostError(val message: String) : HomeState()
}