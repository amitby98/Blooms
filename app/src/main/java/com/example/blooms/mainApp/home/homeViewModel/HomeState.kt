package com.example.blooms.mainApp.home.homeViewModel

import com.example.blooms.model.HomePagePosts
import com.example.blooms.model.Post

sealed class HomeState {
    object Loading : HomeState()
    data class GetAllPostsSuccess(val posts: List<HomePagePosts>) : HomeState()
    data class GetAllGoalsError(val message: String) : HomeState()
}