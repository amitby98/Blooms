package com.example.blooms.allMyPosts

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.blooms.R
import com.example.blooms.addNewPost.step1.AddNewPostStep1FragmentDirections
import com.example.blooms.general.Constance.ADD_NEW_POST_FROM_GOAL
import com.example.blooms.model.Goal


class AllMyPostsActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_my_posts)
        // Obtain reference to the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.add_new_post_nav_host_fragment) as NavHostFragment
        // Get the NavController
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}