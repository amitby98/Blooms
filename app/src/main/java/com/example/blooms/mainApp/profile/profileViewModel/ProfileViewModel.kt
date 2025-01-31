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


    fun saveUser(user: User) {
        viewModelScope.launch {
            repository.saveUser(user)
                .onSuccess {
                    _profileState.value = ProfileState.SaveUserDataSuccess
                }
                .onFailure { exception ->
                    _profileState.value = ProfileState.GetUserDataError(exception.message ?: "Sign in failed")
                }
        }
    }


    fun signOut() {
        repository.signOut()
        _profileState.value = ProfileState.Idle
    }

}