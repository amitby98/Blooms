package com.example.blooms.mainApp.allMyGoal.allMyGoalRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AllMyGoalRepository {

    private val database = FirebaseDatabase.getInstance().getReference("goals")
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    suspend fun getAllMyGoals() : Result<List<Goal>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = database.orderByChild("userId").equalTo(userId).get().await()
                if (snapshot.exists()) {
                    val allMyGoals = snapshot.children.mapNotNull { it.getValue(Goal::class.java) }
                    allMyGoals
                } else {
                    emptyList()
                }
            }
        }
    }
