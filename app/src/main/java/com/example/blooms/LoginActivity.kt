package com.example.blooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView

class LoginActivity : AppCompatActivity() {

    private lateinit var mMoveToRegisterScreenBtn : AppCompatTextView
    private lateinit var mLoginAnimationView : LottieAnimationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login)

        mMoveToRegisterScreenBtn = findViewById(R.id.signUpText)
        mLoginAnimationView = findViewById(R.id.welcomeAnimation)

        mLoginAnimationView.playAnimation()

        mMoveToRegisterScreenBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}