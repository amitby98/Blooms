package com.example.blooms.allMyPosts.editPost.allMyPostsEditViewModel

sealed class AllMyPostsEditState {
    object Loading : AllMyPostsEditState()
    object UpdatePostSuccess : AllMyPostsEditState()
    data class UpdatePostError(val message: String) : AllMyPostsEditState()
}