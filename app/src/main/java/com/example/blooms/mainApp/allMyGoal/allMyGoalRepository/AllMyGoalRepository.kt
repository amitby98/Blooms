package com.example.blooms.mainApp.allMyGoal.allMyGoalRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import com.example.blooms.model.User
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllMyGoalRepository {
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


    suspend fun getAllMyGoals() : Result<List<Goal>> =
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

    suspend fun deleteGoal(goalId : String) : Result<Boolean> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                goalsReference.child(goalId).removeValue().await()
                true
                }
            }
}
