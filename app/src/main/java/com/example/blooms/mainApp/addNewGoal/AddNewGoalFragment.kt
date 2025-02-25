package com.example.blooms.mainApp.addNewGoal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.PrimaryKey
import com.example.blooms.R
import com.example.blooms.general.Constance
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.ImagePickerHelper
import com.example.blooms.general.ImageUtils
import com.example.blooms.general.LoadingDialog
import com.example.blooms.general.SuccessDialog
import com.example.blooms.general.showCustomToast
import com.example.blooms.general.showDatePicker
import com.example.blooms.mainApp.addNewGoal.adapter.CategoryAdapter
import com.example.blooms.mainApp.addNewGoal.addGoalViewModel.AddGoalState
import com.example.blooms.mainApp.addNewGoal.addGoalViewModel.AddGoalViewModel
import com.example.blooms.mainApp.addNewGoal.goalStep.GoalsDialog
import com.example.blooms.model.Category
import com.example.blooms.model.Goal
import com.example.blooms.model.GoalStep
import com.example.blooms.model.Post
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddNewGoalFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private var categories = mutableListOf<Category>()
    private var categorySelected = Category()
    private lateinit var mTitleInput : TextInputEditText
    private lateinit var mMessageInput : TextInputEditText
    private lateinit var mDeadlineInput : TextInputEditText
    private lateinit var saveButton: MaterialButton
    private lateinit var mAddImageBtn: AppCompatButton
    private lateinit var mAddGoalBtn: AppCompatButton
    private lateinit var mImagePost: AppCompatImageView
    private val viewModel: AddGoalViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var imagePickerHelper: ImagePickerHelper
    private var goalList = arrayListOf<GoalStep>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_goal, container, false)
        initializeViews(view)
        setCategoryList()
        setupClickListeners()
        setupObservers()
        setImagePickerHelper()
        return view
    }

    private fun setImagePickerHelper() {
        imagePickerHelper = ImagePickerHelper(
            this,
            onImagePicked = { bitmap, uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(mImagePost)
                } else if (bitmap != null) {
                    mImagePost.setImageBitmap(bitmap)
                }
                mImagePost.visibility = View.VISIBLE
            },
            onPermissionDenied = {
                showCustomToast("Permissions denied, unable to choose image")
            }
        )
    }


    private fun setupObservers() {
        viewModel.addGoalState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddGoalState.UploadPostSuccess -> {
                   loadingDialog.dismiss()
                    val customPopup = SuccessDialog(requireActivity())
                    customPopup.show(
                        "Success",
                        "Your post has been sent to followers",
                        "Close"
                    )
                }
                is AddGoalState.UploadPostError -> {
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
                uploadGoal()
            }
        }

        mAddImageBtn.setOnClickListener {
            imagePickerHelper.openGallery()
        }

        mAddGoalBtn.setOnClickListener {
            val dialog = GoalsDialog(goalList,{
                goalList = it
            })
            dialog.show(parentFragmentManager, "GoalDialogFragment")
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

    private fun uploadGoal() {

        //Create first post from our goal
        val newTitle = mTitleInput.text?.toString()?.trim() ?: ""
        val newMessage = mMessageInput.text?.toString()?.trim() ?: ""
        val bitmap = mImagePost.drawable.toBitmap()
        val postImageString = ImageUtils.convertBitmapToBase64(bitmap)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        var newPost = Post(userId = userId ,title = newTitle, message = newMessage, image = postImageString)

        val newDeadlineDate = mDeadlineInput.text?.toString()?.trim() ?: ""
        val categoryId = categorySelected.id
        val goalStep = goalList
        val postsList = ArrayList<Post>()
        postsList.add(newPost)

        val newGoal = Goal(userId = userId, categoryId = categoryId,
            deadlineDate = newDeadlineDate, posts = postsList, goalStep = goalList )

        loadingDialog.show()
        viewModel.uploadPost(newGoal)
    }

    private fun initializeViews(view: View) {
        recyclerView = view.findViewById(R.id.goal_recycler_category)
        mTitleInput = view.findViewById(R.id.etTitle)
        mMessageInput = view.findViewById(R.id.etBody)
        mDeadlineInput = view.findViewById(R.id.etDeadline)
        saveButton = view.findViewById(R.id.saveButton)
        mAddImageBtn = view.findViewById(R.id.add_image_btn)
        mImagePost = view.findViewById(R.id.image_post)
        mAddGoalBtn = view.findViewById(R.id.add_goal_btn)
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
        fun newInstance() = AddNewGoalFragment()
    }
}