package com.example.blooms.mainApp.allMyTarget.allMyTargetViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.allMyTarget.allMyTargetRepository.AllMyTargetRepository
import kotlinx.coroutines.launch


class AllMyTargetViewModel : ViewModel() {
    private val repository = AllMyTargetRepository()

    private val _allMyTargetState = MutableLiveData<AllMyTargetState>()
    val allMyTargetState: LiveData<AllMyTargetState> = _allMyTargetState

    fun getAllMyTarget() {
        viewModelScope.launch {
            repository.getAllMyPosts()
                .onSuccess { posts ->
                    if (posts.isNotEmpty()) {
                        _allMyTargetState.value = AllMyTargetState.GetAllMyPostSuccess(posts)
                    }
                }
                .onFailure { exception ->
                    _allMyTargetState.value = AllMyTargetState.GetAllMyPostError(exception.message ?: "Sign in failed")
                }
        }
    }
}