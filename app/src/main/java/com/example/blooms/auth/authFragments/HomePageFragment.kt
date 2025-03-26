package com.example.blooms.auth.authFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooms.R
import com.example.blooms.auth.authViewModel.AuthViewModel
import com.example.blooms.general.REMEMBER_MY_LOGIN
import com.example.blooms.general.SharedPrefsHelper
import com.example.blooms.mainApp.MainAppActivity
import com.google.android.material.button.MaterialButton

const val LOGIN = 1
const val REGISTER = 2


class HomePageFragment : Fragment() {
    private lateinit var mMainLottieImage: AppCompatImageView
    private lateinit var mMainTitle: AppCompatTextView
    private lateinit var mMainSubtitle: AppCompatTextView
    private lateinit var mLoginButton: MaterialButton
    private lateinit var mSignupButton: MaterialButton
    private val viewModel: AuthViewModel by viewModels()


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
        if(viewModel.getCurrentUser()
            && SharedPrefsHelper(requireContext()).get(REMEMBER_MY_LOGIN, false)
        ) {
            activity?.startActivity(Intent(requireActivity(), MainAppActivity::class.java))
        }
        setupClickListeners()

        return view
    }

    private fun setupClickListeners() {
        mLoginButton.setOnClickListener {
            handleButtonClick(LOGIN)
        }


        mSignupButton.setOnClickListener {
            handleButtonClick(REGISTER)
        }
    }

    private fun handleButtonClick(moveTo: Int) {
        when(moveTo) {
            LOGIN -> findNavController().navigate(R.id.action_homePageFragment_to_loginFragment)
            REGISTER -> findNavController().navigate(R.id.action_homePageFragment_to_registerFragment)
        }
    }

    companion object {
        fun newInstance() = HomePageFragment()
    }

}