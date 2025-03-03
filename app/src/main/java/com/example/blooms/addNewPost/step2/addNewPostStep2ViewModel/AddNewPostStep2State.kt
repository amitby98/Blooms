package com.example.blooms.addNewPost.step2.addNewPostStep2ViewModel

sealed class AddNewPostStep2State {
    object Loading : AddNewPostStep2State()
    object UpdatePostSuccess : AddNewPostStep2State()
    data class UpdatePostError(val message: String) : AddNewPostStep2State()
}