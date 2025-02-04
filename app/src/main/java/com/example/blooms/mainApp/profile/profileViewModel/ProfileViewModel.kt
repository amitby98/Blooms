package com.example.blooms.mainApp.profile.profileViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.profile.profileRepository.ProfileRepository
import com.example.blooms.model.User
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository()

    private val _profileState = MutableLiveData<ProfileState>()
    val profileState: LiveData<ProfileState> = _profileState

    fun getUserData() {
        viewModelScope.launch {
            repository.getUserData()
                .onSuccess { user ->
                    _profileState.value = ProfileState.GetUserDataSuccess(user = user)
                }
                .onFailure { exception ->
                    _profileState.value = ProfileState.GetUserDataError(exception.message ?: "")
                }
        }
    }

    fun saveUserData(user: User) {
        viewModelScope.launch {
            repository.saveUserData(user)
                .onSuccess {
                    _profileState.value = ProfileState.SaveUserDataSuccess
                }
                .onFailure { exception ->
                    _profileState.value = ProfileState.GetUserDataError(exception.message ?: "Sign in failed")
                }
        }
    }


     fun updateUserEmail(newEmail: String) {
         viewModelScope.launch {
             repository.updateUserEmail(newEmail)
                 .onSuccess {
                     _profileState.value = ProfileState.UpdateEmailSuccess
                 }
                 .onFailure { exception ->
                     _profileState.value = ProfileState.GetUserDataError(exception.message ?: "Update Email Error")
                 }
         }
    }

    fun updateUserPassword(newPassword: String) {
        viewModelScope.launch {
            repository.updateUserPassword(newPassword)
                .onSuccess {
                    _profileState.value = ProfileState.UpdatePasswordSuccess
                }
                .onFailure { exception ->
                    _profileState.value = ProfileState.UpdatePasswordError(exception.message ?: "Update Password Error")
                }
        }
    }


    fun signOut() {
        repository.signOut()
        _profileState.value = ProfileState.Idle
    }

}