package com.example.blooms.mainApp.profile.profileRepository

import com.example.blooms.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference: DatabaseReference = database.getReference("users")
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: throw Exception("User not authenticated")

    suspend fun saveUser(user: User): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                userReference.child(userId).setValue(user)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun getUserData(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun signIn(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}