package com.example.blooms.mainApp.addNewGoal.addGoalRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddGoalRepository(context: Context) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val goalReference = database.getReference("goals")

    private val goalDao = AppDatabase.getDatabase(context).goalDao()

    // Generate a unique ID using push()
    var goalId: String? = goalReference.push().getKey()
    var postId: String? = goalReference.push().getKey()

    suspend fun uploadGoal(goal: Goal): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                if(goalId.isNullOrEmpty() || postId.isNullOrEmpty()) {
                    false
                } else {
                    goal.goalId = goalId.toString()
                    goal.posts.get(0).postId = postId.toString()
                    goalReference.setValue(goal)
                    goalDao.insertGoal(goal)
                    true
                }
            }
        }


}