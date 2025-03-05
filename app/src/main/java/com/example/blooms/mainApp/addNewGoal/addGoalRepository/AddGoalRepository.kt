package com.example.blooms.mainApp.addNewGoal.addGoalRepository

import android.content.Context
import com.example.blooms.model.Goal
import com.example.blooms.model.User
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddGoalRepository(context: Context) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val goalReference = database.getReference("goals")
    private val userReference = database.getReference("users")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")

    private val goalDao = AppDatabase.getDatabase(context).goalDao()

    // Generate a unique ID using push()
    var goalId: String? = goalReference.push().getKey()
    var postId: String? = goalReference.push().getKey()

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

    suspend fun uploadGoal(goal: Goal): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                if(goalId.isNullOrEmpty() || postId.isNullOrEmpty()) {
                    false
                } else {
                    goal.goalId = goalId.toString()
                    goal.posts.get(0).postId = postId.toString()
                    // Upload to Firebase as a child of "goals" node
                    goalReference.child(goal.goalId).setValue(goal)
                    goalDao.insertGoal(goal)
                    true
                }
            }
        }


}