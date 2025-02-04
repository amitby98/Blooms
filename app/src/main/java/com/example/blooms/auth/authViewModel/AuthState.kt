package com.example.blooms.auth.authViewModel

import com.google.firebase.auth.FirebaseUser

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Success(val user: FirebaseUser) : AuthState
    data class Error(val message: String) : AuthState
}