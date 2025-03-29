package com.example.blooms.mainApp

import SettingsFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.blooms.R
import com.example.blooms.mainApp.addNewGoal.AddNewGoalFragment
import com.example.blooms.mainApp.allMyGoal.AllMyGoalFragment
import com.example.blooms.mainApp.home.HomeFragment
import com.example.blooms.mainApp.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainAppActivity : AppCompatActivity() {

    lateinit var mBottomNavigationView : BottomNavigationView
    lateinit var mAddGoalBtn : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_app_activity)
        mBottomNavigationView = findViewById(R.id.bottomNavigationView)
        mAddGoalBtn = findViewById(R.id.addNewGoalBtn)
        replaceFragment(HomeFragment())
        bottomNavigationItemSelected()
        initAddNewGoalBtn()
    }

    private fun initAddNewGoalBtn() {
        mAddGoalBtn.setOnClickListener {
            replaceFragment(AddNewGoalFragment())
            mBottomNavigationView.selectedItemId = R.id.fab
        }
    }

    private fun bottomNavigationItemSelected() {
        mBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.search -> replaceFragment(AllMyGoalFragment())
                R.id.profile -> replaceFragment(ProfileFragment())
                R.id.settings -> replaceFragment(SettingsFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}