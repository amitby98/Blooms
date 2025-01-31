package com.example.blooms.mainApp.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooms.R
import com.example.blooms.auth.MainActivity
import com.example.blooms.auth.authViewModel.AuthState
import com.example.blooms.auth.authViewModel.AuthViewModel
import com.example.blooms.general.ErrorDialog
import com.example.blooms.mainApp.MainAppActivity
import com.example.blooms.mainApp.profile.profileViewModel.ProfileState
import com.example.blooms.mainApp.profile.profileViewModel.ProfileViewModel
import com.example.blooms.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var backButton: AppCompatImageButton
    private lateinit var saveButton: MaterialButton
    private lateinit var nameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var birthDateInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var logoutButton: AppCompatImageButton
    private val viewModel: ProfileViewModel by viewModels()
    private var isAfterRegistrationScreen = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val args = ProfileFragmentArgs.fromBundle(requireArguments())
        isAfterRegistrationScreen = args.isAfterRegistrationScreen
        initializeViews(view)
        setupClickListeners()
        //TODO: Natali - start shimmer and setFieldsEditableState(false)
        // - if isAfterRegistrationScreen == true -> setFieldsEditableState(true)
        if(isAfterRegistrationScreen) {
            setFieldsEditableState(true)
            passwordInput.visibility = View.GONE
            populateUserData()
        }
        populateUserData()
        setupObservers()
        return view
    }

    private fun initializeViews(view : View) {
        backButton = view.findViewById(R.id.btnBack)
        saveButton = view.findViewById(R.id.saveButton)
        nameInput = view.findViewById(R.id.etName)
        lastNameInput = view.findViewById(R.id.etLastName)
        birthDateInput = view.findViewById(R.id.etBirthDate)
        emailInput = view.findViewById(R.id.etEmail)
        passwordInput = view.findViewById(R.id.etPassword)
        logoutButton = view.findViewById(R.id.logoutButton)
    }

    private fun setupClickListeners() {
//        backButton.setOnClickListener {
//            activity.onBackPressed()
//        }

        saveButton.setOnClickListener {
            handleSaveButtonClick()
        }

        logoutButton.setOnClickListener {
            handleLogout()
        }
    }

    private fun handleSaveButtonClick() {
        if (saveButton.isSelected) {
            saveUserData()
        }
    }

    private fun setFieldsEditableState(isEditable: Boolean) {
        nameInput.isEnabled = isEditable
        if(isAfterRegistrationScreen) {
            emailInput.isEnabled = false
        } else {
            emailInput.isEnabled = isEditable
        }
        lastNameInput.isEnabled = isEditable
        birthDateInput.isEnabled = isEditable
        passwordInput.isEnabled = isEditable
        saveButton.isSelected = isEditable
    }

    private fun populateUserData() {
        //TODO: Natali - need to add service that return user data from firebase database - only if isAfterRegistrationScreen is false
        val currentUser = mAuth.currentUser

        currentUser?.let { user ->
            emailInput.setText(user.email)
            if(!isAfterRegistrationScreen) {
                passwordInput.setText("********")
            }
        }
    }


    private fun setupObservers() {
        viewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.SaveUserDataSuccess -> {
                    if(isAfterRegistrationScreen){
                        activity?.startActivity(Intent(requireActivity(), MainAppActivity::class.java))
                    } else {

                    }
                }
                is ProfileState.SaveUserDataError -> {
                    val customPopup = ErrorDialog(requireActivity())
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


    private fun saveUserData() {
        val currentUser = mAuth.currentUser
        val newName = nameInput.text.toString().trim()
        val lastName = lastNameInput.text.toString().trim()
        val birthDate = birthDateInput.text.toString().trim()
        var newEmail = emailInput.text.toString().trim()

        //TODO: Natali - need to handle with check fields - newName, lastName
        if(newName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty() || newEmail.isEmpty()) {
            showToast("Some fields are empty")
            return
        }
        val userData = User(newName,lastName,birthDate,newEmail )

        if(!isAfterRegistrationScreen) {
            newEmail = emailInput.text.toString().trim()
            val newPassword = passwordInput.text.toString()

            currentUser?.let { user ->
                if (newEmail != user.email || newPassword != "********") {
                    if(newPassword.isEmpty()) {
                        showToast("Password field is empty")
                        return
                    }
                    val credential = EmailAuthProvider.getCredential(user.email ?: "", newPassword)
                    user.reauthenticate(credential)
                        .addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                performUserUpdates(user, newEmail, newPassword)
                                updateUserProfile(userData)
                            } else {
                                showToast("Authentication failed: ${reAuthTask.exception?.message}")
                                setFieldsEditableState(false)
                            }
                        }
                } else {
                    updateUserProfile(userData)
                }
            }
        } else {
            updateUserProfile(userData)
        }
    }

    private fun updateUserProfile(user : User) {
        viewModel.saveUser(user)
    }

    private fun performUserUpdates(user: FirebaseUser, newEmail: String, newPassword: String) {

        if (newEmail != user.email) {
            updateUserEmail(user, newEmail)
        }

        if (newPassword != "********") {
            updateUserPassword(user, newPassword)
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun handleLogout() {
        mAuth.signOut()
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
    }
}