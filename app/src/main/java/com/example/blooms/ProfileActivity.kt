package com.example.blooms

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser

class ProfileActivity : AppCompatActivity() {

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

        mAuth = FirebaseAuth.getInstance()

        initializeViews()
        setupClickListeners()
        populateUserData()
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.btnBack)
        editButton = findViewById(R.id.btnEdit)
        nameInput = findViewById(R.id.etName)
        emailInput = findViewById(R.id.etEmail)
        passwordInput = findViewById(R.id.etPassword)
        logoutButton = findViewById(R.id.logoutButton)

        setFieldsEditableState(false)
    }

    private fun setupClickListeners() {
        backButton.setOnClickListener {
            onBackPressed()
        }

        editButton.setOnClickListener {
            handleEditButtonClick()
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }
    }

    private fun handleEditButtonClick() {
        if (editButton.isSelected) {
            saveUserData()
        } else {
            setFieldsEditableState(true)
        }
        editButton.isSelected = !editButton.isSelected
    }

    private fun setFieldsEditableState(isEditable: Boolean) {
        nameInput.isEnabled = isEditable
        emailInput.isEnabled = isEditable
        passwordInput.isEnabled = false  // הסיסמה תמיד לא ניתנת לעריכה ישירה
    }

    private fun populateUserData() {
        val currentUser = mAuth.currentUser

        currentUser?.let { user ->
            // מילוי הנתונים מהמשתמש המחובר
            emailInput.setText(user.email)
            nameInput.setText(user.displayName)
            passwordInput.setText("********")
        }
    }

    private fun saveUserData() {
        val currentUser = mAuth.currentUser
        val newName = nameInput.text.toString().trim()
        val newEmail = emailInput.text.toString().trim()
        val newPassword = passwordInput.text.toString()

        currentUser?.let { user ->
            // בדיקה אם יש שינויים שדורשים אימות מחדש
            if (newEmail != user.email || newPassword != "********") {
                showPasswordConfirmationDialog { currentPassword ->
                    val credential = EmailAuthProvider.getCredential(user.email ?: "", currentPassword)

                    user.reauthenticate(credential)
                        .addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                // עדכון כל השדות לאחר אימות מוצלח
                                performUserUpdates(user, newName, newEmail, newPassword)
                            } else {
                                showToast("Authentication failed: ${reAuthTask.exception?.message}")
                                setFieldsEditableState(false)
                            }
                        }
                }
            } else {
                updateUserProfile(user, newName)
            }
        }
    }

    private fun performUserUpdates(user: FirebaseUser, newName: String, newEmail: String, newPassword: String) {
        updateUserProfile(user, newName)

        // עדכון האימייל אם השתנה
        if (newEmail != user.email) {
            updateUserEmail(user, newEmail)
        }

        // עדכון הסיסמה אם השתנתה
        if (newPassword != "********") {
            updateUserPassword(user, newPassword)
        }
    }

    private fun showPasswordConfirmationDialog(onConfirm: (String) -> Unit) {
        val dialogView = LayoutInflater.from(this)
            .inflate(R.layout.dialog_confirm_password, null)
        val passwordInput = dialogView.findViewById<TextInputEditText>(R.id.passwordConfirmInput)

        MaterialAlertDialogBuilder(this)
            .setTitle("Confirm Changes")
            .setMessage("Please enter your current password to confirm changes")
            .setView(dialogView)
            .setPositiveButton("Confirm") { dialog, _ ->
                val password = passwordInput.text?.toString() ?: ""
                if (password.isNotEmpty()) {
                    onConfirm(password)
                } else {
                    showToast("Password cannot be empty")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
                setFieldsEditableState(false)
            }
            .setCancelable(false)
            .show()
    }

    private fun updateUserProfile(user: FirebaseUser, newName: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(newName)
            .build()

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Profile updated successfully")
                    setFieldsEditableState(false)
                } else {
                    showToast("Failed to update profile: ${task.exception?.message}")
                }
            }
    }

    private fun updateUserEmail(user: FirebaseUser, newEmail: String) {
        user.updateEmail(newEmail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Email updated successfully")
                } else {
                    showToast("Failed to update email: ${task.exception?.message}")
                }
            }
    }

    private fun updateUserPassword(user: FirebaseUser, newPassword: String) {
        user.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Password updated successfully")
                    passwordInput.setText("********")
                } else {
                    showToast("Failed to update password: ${task.exception?.message}")
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleLogout() {
        mAuth.signOut()
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}