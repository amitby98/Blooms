package com.example.blooms

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class ProfileActivity : AppCompatActivity() {

    // הגדרת המשתנים הגלובליים
    private lateinit var mAuth: FirebaseAuth
    private lateinit var backButton: AppCompatImageButton
    private lateinit var editButton: AppCompatImageButton
    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var logoutButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // אתחול Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // אתחול הרכיבים
        initializeViews()
        // הגדרת המאזינים לכפתורים
        setupClickListeners()
        // מילוי המידע של המשתמש
        populateUserData()
    }

    private fun initializeViews() {
        // קישור כל הרכיבים מהלייאאוט
        backButton = findViewById(R.id.btnBack)
        editButton = findViewById(R.id.btnEdit)
        nameInput = findViewById(R.id.etName)
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        logoutButton = findViewById(R.id.logoutButton)

        // הגדרת מצב ההתחלתי של שדות הטקסט כלא ניתנים לעריכה
        setFieldsEditableState(false)
    }

    private fun setupClickListeners() {
        // כפתור חזרה
        backButton.setOnClickListener {
            onBackPressed()
        }

        // כפתור עריכה/שמירה
        editButton.setOnClickListener {
            handleEditButtonClick()
        }

        // כפתור התנתקות
        logoutButton.setOnClickListener {
            handleLogout()
        }
    }

    private fun handleEditButtonClick() {
        // בדיקה האם הכפתור במצב עריכה או שמירה
        if (editButton.isSelected) {
            // במצב שמירה - שומרים את השינויים
            saveUserData()
        } else {
            // במצב עריכה - מאפשרים עריכה
            setFieldsEditableState(true)
        }
        // החלפת מצב הכפתור (וגם האייקון דרך ה-selector)
        editButton.isSelected = !editButton.isSelected
    }

    private fun setFieldsEditableState(isEditable: Boolean) {
        // הגדרת מצב העריכה של השדות
        nameInput.isEnabled = isEditable
        emailInput.isEnabled = isEditable
        // שדה הסיסמה תמיד נשאר לא ניתן לעריכה
        passwordInput.isEnabled = false
    }

    private fun populateUserData() {
        // קבלת המשתמש הנוכחי מפיירבייס
        val currentUser = mAuth.currentUser

        currentUser?.let { user ->
            // הצגת האימייל
            emailInput.setText(user.email)

            // הצגת השם אם קיים
            user.displayName?.let { name ->
                nameInput.setText(name)
            }

            // הצגת כוכביות במקום הסיסמה האמיתית
            passwordInput.setText("********")
        }
    }

    private fun saveUserData() {
        val currentUser = mAuth.currentUser
        val newName = nameInput.text.toString()
        val newEmail = emailInput.text.toString()

        currentUser?.let { user ->
            // עדכון השם
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        // חזרה למצב קריאה בלבד
                        setFieldsEditableState(false)
                    } else {
                        Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

            // אם האימייל השתנה, מעדכנים גם אותו
            if (newEmail != user.email) {
                user.updateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Email updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Failed to update email", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun handleLogout() {
        // התנתקות מפיירבייס
        mAuth.signOut()

        // חזרה למסך הראשי
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}