package com.example.blooms.mainApp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.blooms.R
import com.example.blooms.mainApp.addNewTarget.AddNewTargetFragment
import com.example.blooms.mainApp.allMyTarget.AllMyTargetFragment
import com.example.blooms.mainApp.home.ShowAllTargetFragment
import com.example.blooms.mainApp.profile.ProfileFragment
import com.example.blooms.mainApp.settings.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainAppActivity : AppCompatActivity() {

    lateinit var mBottomNavigationView : BottomNavigationView
    lateinit var mAddTargetBtn : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_app_activity)
        mBottomNavigationView = findViewById(R.id.bottomNavigationView)
        mAddTargetBtn = findViewById(R.id.addNewTargetBtn)
        replaceFragment(ShowAllTargetFragment())
        bottomNavigationItemSelected()
        initAddNewTargetBtn()
    }

    private fun initAddNewTargetBtn() {
        mAddTargetBtn.setOnClickListener {
            replaceFragment(AddNewTargetFragment())
        }
    }

    private fun bottomNavigationItemSelected() {
        mBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(ShowAllTargetFragment())
                R.id.search -> replaceFragment(AllMyTargetFragment())
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