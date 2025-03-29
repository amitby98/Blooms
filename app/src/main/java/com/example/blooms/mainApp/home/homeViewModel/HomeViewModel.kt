package com.example.blooms.mainApp.home.homeViewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.blooms.mainApp.home.homeRepository.HomeRepository
import com.example.blooms.model.Goal
import com.example.blooms.model.HomePagePosts
import com.example.blooms.model.Post
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class HomeViewModel(application: Application) : AndroidViewModel(application)  {
    private val repository = HomeRepository(application)

    private val _homeState = MutableLiveData<HomeState>()
    val homeState: LiveData<HomeState> = _homeState

    fun getAllPosts() {
        _homeState.value = HomeState.Loading
        viewModelScope.launch {
            repository.getAllGoals()
                .onSuccess { goals ->
                        val allPosts = getAllRelevantPosts(goals)
                        _homeState.value = HomeState.GetAllPostsSuccess(allPosts)
                }
                .onFailure { exception ->
                    _homeState.value = HomeState.GetAllGoalsError(exception.message ?: "All Goals failed")
                }
        }
    }


//    private fun getAllRelevantPosts(goalsList : List<Goal>) : List<HomePagePosts> {
//        val postsList = arrayListOf<HomePagePosts>()
//        goalsList.forEach { goal ->
//            if(goal.shareGoal) {
//                val maxPost = goal.posts.maxByOrNull { it.postDateAndTime }
//                maxPost?.let { post ->
//                    val homePagePost = HomePagePosts(
//                        userName = goal.userName,
//                        userProfileImage = goal.userImage,
//                        post = post
//                    )
//                    postsList.add(homePagePost)
//                }
//            }
//        }
//        return postsList.sortedByDescending { it.post.postDateAndTime }
//    }


    private fun getAllRelevantPosts(goalsList : List<Goal>) : List<HomePagePosts> {
        val postsList = arrayListOf<HomePagePosts>()
        goalsList.forEach { goal ->
            if(goal.shareGoal) {
                goal.posts.forEach {
                    val homePagePost = HomePagePosts(
                        userName = goal.userName,
                        userProfileImage = goal.userImage,
                        post = it
                    )
                    postsList.add(homePagePost)
                }
            }
        }
        return postsList.sortedByDescending { it.post.postDateAndTime }
    }

}