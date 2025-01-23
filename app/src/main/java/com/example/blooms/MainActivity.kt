package com.example.blooms

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.blooms.fragments.LoginFragment
import com.example.blooms.fragments.RegisterFragment
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    // הגדרת המשתנים בראש הקלאס
    private lateinit var mMainLottieImage: AppCompatImageView
    private lateinit var mMainTitle: AppCompatTextView
    private lateinit var mMainSubtitle: AppCompatTextView
    private lateinit var mLoginButton: MaterialButton
    private lateinit var mSignupButton: MaterialButton
    private lateinit var mFragmentContainer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // אתחול כל המשתנים
        initializeViews()
        // הגדרת המאזינים לכפתורים
        setupClickListeners()
    }

    private fun initializeViews() {
        // מציאת כל הרכיבים בלייאאוט
        mMainLottieImage = findViewById(R.id.main_activity_mainpage_lottie)
        mMainTitle = findViewById(R.id.main_activity_title)
        mMainSubtitle = findViewById(R.id.main_activity_subtitle)
        mLoginButton = findViewById(R.id.main_activity_login_button)
        mSignupButton = findViewById(R.id.main_activity_signup_button)
        mFragmentContainer = findViewById(R.id.fragment_container)
    }

    private fun setupClickListeners() {
        mLoginButton.setOnClickListener { handleButtonClick(it) }
        mSignupButton.setOnClickListener { handleButtonClick(it) }
    }

    private fun handleButtonClick(view: View) {
        // הסתרת כל הרכיבים של המסך הראשי
        hideMainScreenViews()
        // הצגת מיכל הפרגמנטים
        mFragmentContainer.visibility = View.VISIBLE

        // בחירת הפרגמנט המתאים לפי הכפתור שנלחץ
        when (view.id) {
            R.id.main_activity_login_button -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoginFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.main_activity_signup_button -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, RegisterFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    private fun hideMainScreenViews() {
        // הסתרת כל הרכיבים של המסך הראשי
        mMainLottieImage.visibility = View.GONE
        mMainTitle.visibility = View.GONE
        mMainSubtitle.visibility = View.GONE
        mLoginButton.visibility = View.GONE
        mSignupButton.visibility = View.GONE
    }

    private fun showMainScreenViews() {
        // החזרת הנראות של כל הרכיבים של המסך הראשי
        mMainLottieImage.visibility = View.VISIBLE
        mMainTitle.visibility = View.VISIBLE
        mMainSubtitle.visibility = View.VISIBLE
        mLoginButton.visibility = View.VISIBLE
        mSignupButton.visibility = View.VISIBLE
        mFragmentContainer.visibility = View.GONE
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            // אם יש פרגמנטים במחסנית, נחזור אחורה
            supportFragmentManager.popBackStack()
            // אם זה הפרגמנט האחרון, נחזיר את המסך הראשי
            if (supportFragmentManager.backStackEntryCount == 1) {
                showMainScreenViews()
            }
        } else {
            super.onBackPressed()
        }
    }
}