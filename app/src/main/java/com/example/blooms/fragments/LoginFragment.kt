package com.example.blooms.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.blooms.ProfileActivity
import com.example.blooms.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    // הגדרת המשתנים שנצטרך בפרגמנט
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mMoveToRegisterScreenBtn: AppCompatTextView
    private lateinit var mLoginAnimationView: LottieAnimationView
    private lateinit var mUsername: TextInputEditText
    private lateinit var mPassword: TextInputEditText
    private lateinit var mLoginButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // אתחול FirebaseAuth
        mAuth = FirebaseAuth.getInstance()

        // קישור כל הרכיבים מהממשק למשתנים
        initializeViews(view)

        // הפעלת אנימציית הכניסה
        mLoginAnimationView.playAnimation()

        // הגדרת המאזינים לכפתורים
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        mMoveToRegisterScreenBtn = view.findViewById(R.id.signUpText)
        mLoginAnimationView = view.findViewById(R.id.welcomeAnimation)
        mUsername = view.findViewById(R.id.usernameEditText)  // וודאי שה-ID מתאים ל-layout שלך
        mPassword = view.findViewById(R.id.passwordEditText)  // וודאי שה-ID מתאים ל-layout שלך
        mLoginButton = view.findViewById(R.id.loginButton)  // וודאי שה-ID מתאים ל-layout שלך
    }

    private fun setupClickListeners() {
        // מאזין למעבר למסך ההרשמה
        mMoveToRegisterScreenBtn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment())
                .addToBackStack(null)
                .commit()
        }

        // מאזין לכפתור ההתחברות
        mLoginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun performLogin() {
        val username = mUsername.text.toString().trim()
        val password = mPassword.text.toString()

        // בדיקה שהשדות לא ריקים
        if (username.isNotEmpty() && password.isNotEmpty()) {
            // ניסיון התחברות באמצעות Firebase
            mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // התחברות הצליחה
                        Log.d("LoginFragment", "Login successful")
                        Toast.makeText(
                            requireContext(),
                            "Login successful!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // מעבר למסך הפרופיל
                        navigateToProfile()
                    } else {
                        // התחברות נכשלה
                        Log.e("LoginFragment", "Login failed", task.exception)
                        Toast.makeText(
                            requireContext(),
                            "Login failed: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        } else {
            // הודעה למשתמש שיש למלא את כל השדות
            Toast.makeText(
                requireContext(),
                "Please fill in all fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun navigateToProfile() {
        // יצירת Intent למעבר ל-ProfileActivity
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        startActivity(intent)

        // סגירת כל המסכים הקודמים
        requireActivity().finish()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}