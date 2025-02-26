package com.example.blooms.mainApp.home.homeRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeRepository(context: Context) {

    private val database = FirebaseDatabase.getInstance().getReference("goals")
    private val goalDao = AppDatabase.getDatabase(context).goalDao()

    suspend fun getAllGoals() : Result<List<Goal>> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                val snapshot = database.get().await()
                if (snapshot.exists()) {
                    val allGoals = snapshot.children.mapNotNull { it.getValue(Goal::class.java) }
                    goalDao.insertGoals(allGoals)
                    allGoals
                } else {
                    emptyList()
                }
            }
        }
    }
