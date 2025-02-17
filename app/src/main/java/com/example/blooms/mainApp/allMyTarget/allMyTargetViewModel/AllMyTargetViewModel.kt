package com.example.blooms.mainApp.allMyTarget.allMyTargetViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.allMyTarget.allMyTargetRepository.AllMyTargetRepository
import kotlinx.coroutines.launch


class AllMyTargetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AllMyTargetRepository(application)

    private val _allMyTargetState = MutableLiveData<AllMyTargetState>()
    val allMyTargetState: LiveData<AllMyTargetState> = _allMyTargetState

    fun getAllMyTarget() {
        viewModelScope.launch {
            repository.getAllMyPosts()
                .onSuccess { posts ->
                    if (posts.isNotEmpty()) {
                        _allMyTargetState.value = AllMyTargetState.GetAllMyPostSuccess(posts)
                    } else {

                    }
                }
                .onFailure { exception ->
                    _allMyTargetState.value = AllMyTargetState.GetAllMyPostError(exception.message ?: "All My Target Posts failed")
                }
        }
    }
}