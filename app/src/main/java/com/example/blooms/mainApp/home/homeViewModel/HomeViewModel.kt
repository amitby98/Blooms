package com.example.blooms.mainApp.home.homeViewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.allMyTarget.allMyTargetRepository.AllMyTargetRepository
import com.example.blooms.mainApp.home.homeRepository.HomeRepository
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val repository = HomeRepository()

    private val _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> = _homeState

    fun getAllMyTarget() {
        viewModelScope.launch {
            repository.getAllPosts()
                .onSuccess { posts ->
                    if (posts.isNotEmpty()) {
                        _homeState.value = HomeState.GetAllPostSuccess(posts)
                    }
                }
                .onFailure { exception ->
                    _homeState.value = HomeState.GetAllPostError(exception.message ?: "All Target Posts failed")
                }
        }
    }
}