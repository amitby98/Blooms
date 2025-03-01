package com.example.blooms.addNewPost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.blooms.R
import com.example.blooms.mainApp.addNewGoal.AddNewGoalFragment
import com.example.blooms.mainApp.allMyGoal.AllMyGoalFragment
import com.example.blooms.mainApp.home.HomeFragment
import com.example.blooms.mainApp.profile.ProfileFragment
import com.example.blooms.mainApp.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class AddNewPostActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_post)
        // Obtain reference to the NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.add_new_post_nav_host_fragment) as NavHostFragment
        // Get the NavController
        navController = navHostFragment.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // todo: add back support
}