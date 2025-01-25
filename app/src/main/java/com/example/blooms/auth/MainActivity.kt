package com.example.blooms.auth

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.blooms.auth.authFragments.HomePageFragment
import com.example.blooms.auth.authFragments.LoginFragment
import com.example.blooms.auth.authFragments.RegisterFragment
import com.example.blooms.R

class MainActivity : AppCompatActivity() {

    // הגדרת המשתנים בראש הקלאס
    private lateinit var mFragmentContainer: View
    private val loginFragment =  LoginFragment.newInstance()
    private val homePageFragment = HomePageFragment.newInstance()
    private val registerFragment = RegisterFragment.newInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homePageFragment)
        // אתחול כל המשתנים
        initializeViews()
    }

    private fun initializeViews() {
        // מציאת כל הרכיבים בלייאאוט
        mFragmentContainer = findViewById(R.id.fragment_container)
    }

    private fun handleButtonClick(view: View) {
        // הצגת מיכל הפרגמנטים
        mFragmentContainer.visibility = View.VISIBLE

        // בחירת הפרגמנט המתאים לפי הכפתור שנלחץ
        when (view.id) {
            R.id.main_activity_login_button -> {
                replaceFragment(LoginFragment.newInstance())
            }
            R.id.main_activity_signup_button -> {
                replaceFragment(RegisterFragment.newInstance())
            }
        }
    }

    fun replaceFragment(newInstance: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newInstance)
            .addToBackStack(null)
            .commit()
    }

}