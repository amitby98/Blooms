package com.example.blooms.mainApp.AllMyTarget.allMyTargetViewModel

import com.example.blooms.model.Post

sealed class AllMyTargetState {
    object Loading : AllMyTargetState()
    data class GetAllMyPostSuccess(val posts: List<Post>) : AllMyTargetState()
    data class GetAllMyPostError(val message: String) : AllMyTargetState()
}