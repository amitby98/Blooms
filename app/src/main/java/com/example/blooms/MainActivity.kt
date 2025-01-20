package com.example.blooms

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        findViewById<MaterialButton>(R.id.main_activity_login_button).setOnClickListener { handleButtonClick(it) }
        findViewById<MaterialButton>(R.id.main_activity_signup_button).setOnClickListener { handleButtonClick(it) }
    }

    private fun handleButtonClick(view: View) {
        when (view.id) {
            R.id.main_activity_login_button -> navigateToActivity(LoginActivity::class.java)
            R.id.main_activity_signup_button -> navigateToActivity(RegisterActivity::class.java)
        }
    }

    private fun navigateToActivity(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }
}