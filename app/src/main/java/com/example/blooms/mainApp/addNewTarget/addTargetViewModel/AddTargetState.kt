package com.example.blooms.mainApp.addNewTarget.addTargetViewModel

sealed class AddTargetState {
    object Loading : AddTargetState()
    object UploadPostSuccess : AddTargetState()
    data class UploadPostError(val message: String) : AddTargetState()
}