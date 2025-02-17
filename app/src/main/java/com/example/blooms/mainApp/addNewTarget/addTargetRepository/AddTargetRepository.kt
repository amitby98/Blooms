package com.example.blooms.mainApp.addNewTarget.addTargetRepository

import android.content.Context
import com.example.blooms.model.Post
import com.example.blooms.model.dao.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTargetRepository(context: Context) {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val postReference = database.getReference("posts")

    private val postDao = AppDatabase.getDatabase(context).postDao()

    // Generate a unique ID using push()
    val newPostRef = postReference.push()
    val postId = newPostRef.key.toString()  // Unique ID

    suspend fun uploadPost(post: Post): Result<Boolean> =
        withContext(Dispatchers.IO){
            post.postId = postId
            kotlin.runCatching {
                newPostRef.setValue(post)
                postDao.insertPost(post)
                true
            }
        }


}