package com.example.blooms.addNewPost.step2.addNewPostStep2Repository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNewPostStep2Repository(context: Context) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val goalReference = database.getReference("goals")

    private val goalDao = AppDatabase.getDatabase(context).goalDao()

    var postId: String? = goalReference.push().getKey()

    suspend fun updateGoal(goal: Goal, postPosition: Int): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                if(goal.goalId.isEmpty() || postId.isNullOrEmpty()) {
                    false
                } else {
                    val goal_id = goal.goalId
                    goal.posts.get(postPosition).postId = postId.toString()
                    goalReference.child(goal.goalId).setValue(goal)
                    goalDao.insertGoal(goal)
                    true
                }
            }
        }


}