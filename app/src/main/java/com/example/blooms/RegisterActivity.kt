package com.example.blooms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity: AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth;

    private lateinit var mMoveToLoginScreenBtn : AppCompatTextView
    private lateinit var mUsername : TextInputEditText
    private lateinit var mPassword : TextInputEditText
    private lateinit var mRegisterBtn : MaterialButton
    private lateinit var mAnimationView : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_register)

        mMoveToLoginScreenBtn = findViewById(R.id.loginText)
        mUsername = findViewById(R.id.emailEditTextRegister)
        mPassword = findViewById(R.id.passwordEditTextRegister)
        mRegisterBtn = findViewById(R.id.registerButton)
        mAnimationView = findViewById(R.id.registerAnimation)
        mAuth = FirebaseAuth.getInstance()


        mAnimationView.playAnimation()
        initClick()
    }

    private fun initClick(){
        mMoveToLoginScreenBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        mRegisterBtn.setOnClickListener{
            val username = mUsername.text.toString()
            val password = mPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()){
                mAuth.createUserWithEmailAndPassword(username,password)
                    .addOnCompleteListener(this){task ->
                        if (task.isSuccessful){

                        } else{

                        }
                    }
            }
        }
    }
}