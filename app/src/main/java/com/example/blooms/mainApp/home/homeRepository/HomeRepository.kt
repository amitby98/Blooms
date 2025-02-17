package com.example.blooms.mainApp.home.homeRepository

import android.content.Context
import com.example.blooms.model.Post
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeRepository(context: Context) {

    private val database = FirebaseDatabase.getInstance().getReference("posts")
    private val postDao = AppDatabase.getDatabase(context).postDao()

    suspend fun getAllPosts() : Result<List<Post>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = database.get().await()
                if (snapshot.exists()) {
                    val allPosts = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
                    postDao.insertPosts(allPosts)
                    allPosts
                } else {
                    emptyList()
                }
            }
        }
    }
