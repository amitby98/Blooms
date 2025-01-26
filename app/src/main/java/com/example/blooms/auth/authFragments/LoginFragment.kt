package com.example.blooms.auth.authFragments

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.blooms.ProfileActivity
import com.example.blooms.R
import com.example.blooms.auth.authViewModel.AuthState
import com.example.blooms.auth.authViewModel.AuthViewModel
import com.example.blooms.general.ErrorDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginFragment : Fragment() {

    private lateinit var mMoveToRegisterScreenBtn: AppCompatTextView
    private lateinit var mLoginAnimationView: LottieAnimationView
    private lateinit var mUsername: TextInputEditText
    private lateinit var mPassword: TextInputEditText
    private lateinit var mLoginButton: MaterialButton
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        mLoginAnimationView.playAnimation()
        setupClickListeners()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthState.Loading -> {}
                is AuthState.Success -> {}
                is AuthState.Error -> {
                    val customPopup = ErrorDialog(requireActivity())

                    // Error popup example
                    customPopup.show(
                        "Oops",
                        state.message,
                        "TRY AGAIN"
                    )
                }
                else -> {}
            }
        }
    }

    private fun initializeViews(view: View) {
        mMoveToRegisterScreenBtn = view.findViewById(R.id.signUpText)
        mLoginAnimationView = view.findViewById(R.id.welcomeAnimation)
        mUsername = view.findViewById(R.id.usernameEditText)
        mPassword = view.findViewById(R.id.passwordEditText)
        mLoginButton = view.findViewById(R.id.loginButton)
    }

    private fun setupClickListeners() {
        mMoveToRegisterScreenBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        mLoginButton.setOnClickListener {
            performLogin()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Invalid email address", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (password.isEmpty() || password.length < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun performLogin() {
        val username = mUsername.text.toString().trim()
        val password = mPassword.text.toString()

        if (validateInput(username,password)) {
            viewModel.signIn(username, password)
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(requireActivity(), ProfileActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}