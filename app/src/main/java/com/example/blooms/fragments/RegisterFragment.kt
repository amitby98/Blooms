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
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.blooms.ProfileActivity
import com.example.blooms.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterFragment : Fragment() {

    // אתחול Firebase עם lazy loading
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var mMoveToLoginScreenBtn: AppCompatTextView? = null
    private var mUsername: TextInputEditText? = null
    private var mPassword: TextInputEditText? = null
    private var mRegisterBtn: MaterialButton? = null
    private var mAnimationView: LottieAnimationView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // אתחול אסינכרוני של הרכיבים
        viewLifecycleOwner.lifecycleScope.launch {
            initializeViews(view)
            setupClickListeners()
        }
    }

    private suspend fun initializeViews(view: View) {
        withContext(Dispatchers.Main) {
            mUsername = view.findViewById(R.id.emailEditTextRegister)
            mPassword = view.findViewById(R.id.passwordEditTextRegister)
            mRegisterBtn = view.findViewById(R.id.registerButton)
            mMoveToLoginScreenBtn = view.findViewById(R.id.loginText)
        }

        // אתחול האנימציה בנפרד
        withContext(Dispatchers.Main) {
            mAnimationView = view.findViewById(R.id.registerAnimation)
            mAnimationView?.playAnimation()
        }
    }

    private fun setupClickListeners() {
        // מעבר משופר למסך ההתחברות עם אנימציה
        mMoveToLoginScreenBtn?.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        mRegisterBtn?.setOnClickListener {
            performRegistration()
        }
    }

    private fun performRegistration() {
        val username = mUsername?.text.toString().trim()
        val password = mPassword?.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                showLoadingState(true)
                registerUser(username, password)
            }
        } else {
            showToast("Please fill in all fields")
        }
    }

    private fun showLoadingState(isLoading: Boolean) {
        mRegisterBtn?.isEnabled = !isLoading
        // כאן אפשר להוסיף אינדיקטור טעינה אם רוצים
    }

    private suspend fun registerUser(username: String, password: String) {
        try {
            mAuth.createUserWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.user
                        user?.let {
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(username.substringBefore('@'))
                                .build()

                            it.updateProfile(profileUpdates)
                                .addOnCompleteListener { profileTask ->
                                    if (profileTask.isSuccessful) {
                                        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                                            showLoadingState(false)
                                            showToast("Registration successful!")
                                            navigateToProfile()
                                        }
                                    } else {
                                        handleRegistrationError(profileTask.exception
                                            ?: Exception("Failed to update profile"))
                                    }
                                }
                        }
                    } else {
                        handleRegistrationError(task.exception
                            ?: Exception("Registration failed"))
                    }
                }
        } catch (e: Exception) {
            handleRegistrationError(e)
        }
    }

    private fun handleRegistrationError(exception: Exception) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            showLoadingState(false)
            Log.e("RegisterFragment", "Registration failed", exception)
            showToast("Registration failed: ${exception.message}")
        }
    }

    private fun showToast(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}