package com.example.blooms.mainApp.addNewTarget.addTargetViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.addNewTarget.addTargetRepository.AddTargetRepository
import com.example.blooms.model.Post
import kotlinx.coroutines.launch


class AddTargetViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AddTargetRepository(application)

    private val _addTargetState = MutableLiveData<AddTargetState>()
    val addTargetState: LiveData<AddTargetState> = _addTargetState

    fun uploadPost(post: Post) {
        viewModelScope.launch {
            repository.uploadPost(post)
                .onSuccess {
                    _addTargetState.value = AddTargetState.UploadPostSuccess
                }
                .onFailure { exception ->
                    _addTargetState.value = AddTargetState.UploadPostError(exception.message ?: "Sign in failed")
                }
        }
    }
}