package com.example.blooms.auth.authFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.blooms.R
import com.google.android.material.button.MaterialButton

const val LOGIN = 1
const val REGISTER = 2


class HomePageFragment : Fragment() {
    // הגדרת המשתנים בראש הקלאס
    private lateinit var mMainLottieImage: AppCompatImageView
    private lateinit var mMainTitle: AppCompatTextView
    private lateinit var mMainSubtitle: AppCompatTextView
    private lateinit var mLoginButton: MaterialButton
    private lateinit var mSignupButton: MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        mMainLottieImage = view.findViewById(R.id.main_activity_mainpage_lottie)
        mMainTitle = view.findViewById(R.id.main_activity_title)
        mMainSubtitle = view.findViewById(R.id.main_activity_subtitle)
        mLoginButton = view.findViewById(R.id.main_activity_login_button)
        mSignupButton = view.findViewById(R.id.main_activity_signup_button)

        setupClickListeners()

        return view
    }

    private fun setupClickListeners() {
        mLoginButton.setOnClickListener { handleButtonClick(1) }


        mSignupButton.setOnClickListener {
            handleButtonClick(2)
        }
    }

    private fun handleButtonClick(moveTo: Int) {
        var newFragment = Fragment()
        when(moveTo) {
            1 -> newFragment = LoginFragment.newInstance()
            2 -> newFragment = RegisterFragment.newInstance()
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, newFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun handleRegisterButtonClick() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }

}