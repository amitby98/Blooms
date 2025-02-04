package com.example.blooms.auth.authViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.auth.authRepository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val repository = AuthRepository()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signUp(email, password)
                .onSuccess { user ->
                    user?.let {
                        _authState.value = AuthState.Success(it)
                    } ?: run {
                        _authState.value = AuthState.Error("Sign in failed")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
                }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            repository.signIn(email, password)
                .onSuccess { user ->
                    user?.let {
                        _authState.value = AuthState.Success(user)
                    } ?: run {
                        _authState.value = AuthState.Error("Sign up failed")
                    }
                }
                .onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign in failed")
                }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Idle
    }
}