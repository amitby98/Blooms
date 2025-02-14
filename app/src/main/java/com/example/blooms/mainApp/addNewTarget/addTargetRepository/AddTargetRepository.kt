package com.example.blooms.mainApp.addNewTarget.addTargetRepository

import com.example.blooms.model.Post
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTargetRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val postReference = database.getReference("posts")

    // Generate a unique ID using push()
    val newPostRef = postReference.push()
    val postId = newPostRef.key  // Unique ID

    suspend fun uploadPost(post: Post): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                newPostRef.setValue(post)
                true
            }
        }


}