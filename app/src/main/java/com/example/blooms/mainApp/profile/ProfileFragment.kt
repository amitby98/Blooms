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
import com.example.blooms.general.LoadingDialog
import com.example.blooms.mainApp.MainAppActivity
import com.example.blooms.mainApp.profile.profileViewModel.ProfileState
import com.example.blooms.mainApp.profile.profileViewModel.ProfileViewModel
import com.example.blooms.model.User
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    private var mAuth = FirebaseAuth.getInstance()
    private lateinit var saveButton: MaterialButton
    private lateinit var nameInput: TextInputEditText
    private lateinit var lastNameInput: TextInputEditText
    private lateinit var birthDateInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var oldPasswordInput: TextInputEditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var tilPasswordOld: TextInputLayout
    private lateinit var logoutButton: AppCompatImageButton
    private val viewModel: ProfileViewModel by viewModels()
    private var isAfterRegistrationScreen = false
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        try{
            val args = ProfileFragmentArgs.fromBundle(requireArguments())
            isAfterRegistrationScreen = args.isAfterRegistrationScreen

        } catch (e: Exception) {
            isAfterRegistrationScreen = false
        }
        initializeViews(view)
        setupClickListeners()
        if(isAfterRegistrationScreen) {
            setFieldsEditableState(true)
            passwordInput.visibility = View.GONE
            oldPasswordInput.visibility = View.GONE
            tilPasswordOld.visibility = View.GONE
            tilPassword.visibility = View.GONE
            logoutButton.visibility = View.GONE
            populateUserData()
        } else {
            loadingDialog?.show()
        }
        populateUserData()
        setupObservers()
        return view
    }

    private fun initializeViews(view : View) {
        saveButton = view.findViewById(R.id.saveButton)
        nameInput = view.findViewById(R.id.etName)
        lastNameInput = view.findViewById(R.id.etLastName)
        birthDateInput = view.findViewById(R.id.etBirthDate)
        emailInput = view.findViewById(R.id.etEmail)
        passwordInput = view.findViewById(R.id.etPassword)
        oldPasswordInput = view.findViewById(R.id.etOldPassword)
        tilPasswordOld = view.findViewById(R.id.tilOldPassword)
        tilPassword = view.findViewById(R.id.tilPassword)
        logoutButton = view.findViewById(R.id.logoutButton)
        loadingDialog = LoadingDialog(requireContext())
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

        birthDateInput.setOnClickListener {
            showDatePicker()
        }
    }

    private fun handleSaveButtonClick() {
        saveUserData()
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
        oldPasswordInput.isEnabled = isEditable
    }

    private fun populateUserData() {
        if(!isAfterRegistrationScreen) {
            viewModel.getUserData()
        }
        val currentUser = mAuth.currentUser
        currentUser?.let { user ->
            emailInput.setText(user.email)
            if(!isAfterRegistrationScreen) {
                passwordInput.setText("********")
                oldPasswordInput.setText("********")
            }
        }
    }


    private fun setupObservers() {
        viewModel.profileState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.SaveUserDataSuccess -> {
                    loadingDialog.dismiss()
                    if(isAfterRegistrationScreen){
                        activity?.startActivity(Intent(requireActivity(), MainAppActivity::class.java))
                    } else {

                    }
                }
                is ProfileState.SaveUserDataError -> {
                    loadingDialog.dismiss()
                    val customPopup = ErrorDialog(requireActivity())
                    customPopup.show(
                        "Oops",
                        state.message,
                        "TRY AGAIN"
                    )
                }
                is ProfileState.GetUserDataSuccess -> {
                    loadingDialog.dismiss()
                    if(state.user != null) {
                        val user = state.user
                        nameInput.setText(user.firstName)
                        lastNameInput.setText(user.lastName)
                        emailInput.setText(user.email)
                        birthDateInput.setText(user.birthDate)
                    } else {
                        //TODO: show error popup
                    }
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

        if(newName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty() || newEmail.isEmpty()) {
            showToast("Some fields are empty")
            return
        }
        val userData = User(newName,lastName,birthDate,newEmail )

        if(!isAfterRegistrationScreen) {
            newEmail = emailInput.text.toString().trim()
            val newPassword = passwordInput.text.toString()
            val oldPassword = oldPasswordInput.text.toString()

            currentUser?.let { user ->
                if (newEmail != user.email || newPassword != "********") {
                    if(oldPassword == "********") {
                        showToast("Please enter old password")
                        return
                    }
                    if(newPassword.isEmpty()) {
                        showToast("Password field is empty")
                        return
                    }
                    if (newPassword.length < 6) {
                        Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    }

                    val credential = EmailAuthProvider.getCredential(user.email ?: "", oldPassword)
                    user.reauthenticate(credential)
                        .addOnCompleteListener { reAuthTask ->
                            if (reAuthTask.isSuccessful) {
                                performUserUpdates(user, newEmail, newPassword, userData)
                            } else {
                                showToast("Authentication failed: ${reAuthTask.exception?.message}")
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
        viewModel.saveUserData(user)
    }

    private fun performUserUpdates(user: FirebaseUser, newEmail: String, newPassword: String, userData: User) {
        if (newEmail != user.email) {
            viewModel.updateUserEmail(newEmail)
        }

        if (newPassword != "********") {
            viewModel.updateUserPassword(newPassword)
        }
        updateUserProfile(userData)
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDatePicker() {
        // Create MaterialDatePicker instance
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        // Show the Material Date Picker
        materialDatePicker.show(requireActivity().supportFragmentManager, materialDatePicker.toString())

        // Set listener for when the user selects a date
        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            // Format the selected date into a readable format
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = sdf.format(Date(selection))

            // Set the formatted date in the TextInputEditText
            birthDateInput.setText(formattedDate)
        }

        // Handle dismiss or negative button (optional)
        materialDatePicker.addOnNegativeButtonClickListener {
        }
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