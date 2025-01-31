package com.example.blooms.mainApp.profile.profileViewModel

import com.example.blooms.model.User

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object SaveUserDataSuccess : ProfileState()
    data class SaveUserDataError(val message: String) : ProfileState()
    data class GetUserDataSuccess(val user: User) : ProfileState()
    data class GetUserDataError(val message: String) : ProfileState()
}