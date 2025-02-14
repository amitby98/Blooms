package com.example.blooms.mainApp.AllMyTarget.allMyTargetRepository

import android.util.Log
import com.example.blooms.model.Post
import com.example.blooms.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllMyTargetRepository {

    private val database = FirebaseDatabase.getInstance().getReference("posts")
    val userId = FirebaseAuth.getInstance().currentUser?.uid


    suspend fun getAllMyPosts() : Result<List<Post>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = database.orderByChild("userId").equalTo(userId).get().await()
                if (snapshot.exists()) {
                    val allPosts = snapshot.children.mapNotNull { it.getValue(Post::class.java) }
                    allPosts
                } else {
                    emptyList()
                }
            }
        }
    }
