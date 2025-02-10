package com.example.blooms.mainApp.addTarget.addTargetRepository

import com.example.blooms.model.Post
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTargetRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val userReference = database.getReference("posts")

    suspend fun uploadPost(post: Post): Result<Boolean> =
        withContext(Dispatchers.IO){
            kotlin.runCatching {
                userReference.setValue(post)
                true
            }
        }


}