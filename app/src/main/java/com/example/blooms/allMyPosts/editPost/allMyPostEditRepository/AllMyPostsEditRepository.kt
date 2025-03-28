package com.example.blooms.allMyPosts.editPost.allMyPostEditRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AllMyPostsEditRepository(context: Context) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val goalReference = database.getReference("goals")
    private val goalDao = AppDatabase.getDatabase(context).goalDao()


    suspend fun updateGoal(goal: Goal): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                if(goal.goalId.isEmpty()) {
                    false
                } else {
                    goalReference.child(goal.goalId).setValue(goal)
                    goalDao.insertGoal(goal)
                    true
                }
            }
        }


}