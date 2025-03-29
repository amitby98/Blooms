package com.example.blooms.addNewPost

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.example.blooms.R
import com.example.blooms.addNewPost.step1.AddNewPostStep1FragmentDirections
import com.example.blooms.general.Constance.ADD_NEW_POST_FROM_GOAL
import com.example.blooms.model.Goal


class AddNewPostActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_post)
        navController = findNavController(this, R.id.add_new_post_nav_host_fragment)

        // Retrieve the Goal object from the intent
        val goal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(ADD_NEW_POST_FROM_GOAL, Goal::class.java)
        } else {
            intent.getSerializableExtra(ADD_NEW_POST_FROM_GOAL) as Goal
        }

        goal?.let {
            // Pass the Goal object to the fragment via Safe Args
            val action = AddNewPostStep1FragmentDirections.actionAddNewPostActivityToAddNewPostFragment(goal)
            navController.navigate(action)
        }.run {
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}