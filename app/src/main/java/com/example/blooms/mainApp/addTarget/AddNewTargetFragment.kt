package com.example.blooms.mainApp.addTarget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.Constance
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.LoadingDialog
import com.example.blooms.general.SuccessDialog
import com.example.blooms.general.showCustomToast
import com.example.blooms.general.showDatePicker
import com.example.blooms.mainApp.MainAppActivity
import com.example.blooms.mainApp.addTarget.adapter.CategoryAdapter
import com.example.blooms.mainApp.addTarget.addTargetViewModel.AddTargetState
import com.example.blooms.mainApp.addTarget.addTargetViewModel.AddTargetViewModel
import com.example.blooms.mainApp.profile.profileViewModel.ProfileState
import com.example.blooms.mainApp.profile.profileViewModel.ProfileViewModel
import com.example.blooms.model.Category
import com.example.blooms.model.Post
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddNewTargetFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private var categories = mutableListOf<Category>()
    private var categorySelected = Category()
    private lateinit var mTitleInput : TextInputEditText
    private lateinit var mMessageInput : TextInputEditText
    private lateinit var mDeadlineInput : TextInputEditText
    private lateinit var saveButton: MaterialButton
    private val viewModel: AddTargetViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_target, container, false)
        initializeViews(view)
        setCategoryList()
        setupClickListeners()
        setupObservers()
        return view
    }


    private fun setupObservers() {
        viewModel.addTargetState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddTargetState.UploadPostSuccess -> {
                   loadingDialog.dismiss()
                    val customPopup = SuccessDialog(requireActivity())
                    customPopup.show(
                        "Success",
                        "Your post has been sent to followers",
                        "Close"
                    )
                }
                is AddTargetState.UploadPostError -> {
                    loadingDialog.dismiss()
                    val customPopup = ErrorDialog(requireActivity())
                    customPopup.show(
                        "Oops",
                        state.message,
                        "close"
                    )
                }
                else -> {}
            }
        }
    }

    private fun setupClickListeners() {

        mDeadlineInput.setOnClickListener {
            showDatePicker { selectedDate ->
                mDeadlineInput.setText(selectedDate)
            }
        }

        saveButton.setOnClickListener {
            if(checkValidation()) {
                uploadPost()
            }
        }
    }

    private fun checkValidation() : Boolean {
        val newPostTitle = mTitleInput.text?.trim() ?: ""
        val newMessage = mMessageInput.text?.trim() ?: ""
        val newDeadlineDate = mDeadlineInput.text?.trim() ?: ""

        if(newPostTitle.isEmpty() || newMessage.isEmpty() || newDeadlineDate.isEmpty() || categorySelected.id == -1 ) {
            showCustomToast("Please fill all fields")
            return false
        }
        return true
    }

    private fun uploadPost() {
        val newTitle = mTitleInput.text?.toString()?.trim() ?: ""
        val newMessage = mMessageInput.text?.toString()?.trim() ?: ""
        val newDeadlineDate = mDeadlineInput.text?.toString()?.trim() ?: ""
        val categoryId = categorySelected.id
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        var newPost = Post(userId = userId , categoryId = categoryId
            , title = newTitle, message = newMessage,
            deadlineDate = newDeadlineDate, postDateAndTime = getCurrentDateTime())

        loadingDialog.show()
        viewModel.uploadPost(newPost)
    }


    private fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        return current.format(formatter)
    }

    private fun initializeViews(view: View) {
        recyclerView = view.findViewById(R.id.target_recycler_category)
        mTitleInput = view.findViewById(R.id.etTitle)
        mMessageInput = view.findViewById(R.id.etBody)
        mDeadlineInput = view.findViewById(R.id.etDeadline)
        saveButton = view.findViewById(R.id.saveButton)
        loadingDialog = LoadingDialog(requireContext())


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun  setCategoryList() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        categories = mutableListOf(
            Category(Constance.FITNESS,"Fitness", R.drawable.fitness_running_icon),
            Category(Constance.ECONOMY,"Economy", R.drawable.economic),
            Category(Constance.FAMILY,"Family", R.drawable.family_care_father_mother_icon),
            Category(Constance.SHOPPING,"Shopping", R.drawable.shopping_bag),
            Category(Constance.VACATION,"Vacation", R.drawable.vacation),
            Category(Constance.HEALTH,"Health", R.drawable.health_healthcare_medical_icon),
            Category(Constance.OTHER,"Other", R.drawable.other_icon)
        )

        adapter = CategoryAdapter(categories) { selectedPosition ->
            categorySelected = categories.get(selectedPosition)
            categories.forEachIndexed { index, category -> category.isSelected = index == selectedPosition }
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = AddNewTargetFragment()
    }

}