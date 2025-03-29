package com.example.blooms.allMyPosts.Lobby.allMyPostsLobbyRepository

import com.example.blooms.model.Goal
import com.example.blooms.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllMyPostsLobbyRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val goalsReference = FirebaseDatabase.getInstance().getReference("goals")
    private val userReference = database.getReference("users")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")

    suspend fun getUserData(): Result<User?> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = userReference.child(userId).get().await()
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    user
                } else {
                    null
                }
            }
        }


    suspend fun getAllMyPosts() : Result<List<Goal>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = goalsReference.orderByChild("userId").equalTo(userId).get().await()
                if (snapshot.exists()) {
                    val allMyGoals = snapshot.children.mapNotNull { it.getValue(Goal::class.java) }
                    allMyGoals
                } else {
                    emptyList()
                }
            }
        }
    }
