package com.example.blooms.mainApp.allMyGoal.allMyGoalRepository

import android.content.Context
import com.example.blooms.model.Post
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllMyGoalRepository(context: Context) {

    private val database = FirebaseDatabase.getInstance().getReference("goals")
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val goalDao = AppDatabase.getDatabase(context).goalDao()

    suspend fun getAllMyPosts() : Result<List<Post>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = database.orderByChild("userId").equalTo(userId).get().await()
                if (snapshot.exists()) {
                    val allPosts = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
                    //goalDao.insertGoals(allPosts)
                    allPosts
                } else {
                    emptyList()
                }
            }
        }
    }
