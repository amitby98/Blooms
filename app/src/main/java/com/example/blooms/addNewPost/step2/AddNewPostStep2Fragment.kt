package com.example.blooms.addNewPost.step2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.blooms.R
import com.example.blooms.addNewPost.step1.AddNewPostStep1FragmentArgs
import com.example.blooms.addNewPost.step2.addNewPostStep2ViewModel.AddNewPostStep2State
import com.example.blooms.addNewPost.step2.addNewPostStep2ViewModel.AddNewPostStep2ViewModel
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.ImagePickerHelper
import com.example.blooms.general.ImageUtils
import com.example.blooms.general.LoadingDialog
import com.example.blooms.general.SuccessDialog
import com.example.blooms.general.showCustomToast
import com.example.blooms.mainApp.addNewGoal.addGoalViewModel.AddGoalViewModel
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalState
import com.example.blooms.model.Goal
import com.example.blooms.model.Post
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso


class AddNewPostStep2Fragment : Fragment() {

    private lateinit var mCloseBtn: AppCompatImageView
    private lateinit var mBackBtn: AppCompatImageView
    private lateinit var mTitleInput : TextInputEditText
    private lateinit var mMessageInput : TextInputEditText
    private lateinit var mImageButton: AppCompatImageView
    private lateinit var mImagePost: AppCompatImageView
    private lateinit var imagePickerHelper: ImagePickerHelper
    private lateinit var mUpdateButton: MaterialButton
    private val viewModel: AddNewPostStep2ViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    private var mPositionChange: Int = -1
    private lateinit var mGoal : Goal


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_post_step2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
        setImagePickerHelper()
        setupObservers()
        try {
            // Retrieve the Goal object passed from the Activity
            if (arguments != null) {
                val args = AddNewPostStep2FragmentArgs.fromBundle(requireArguments())
                mPositionChange = args.position
                mGoal = args.goal
                mTitleInput.setText(mGoal.goalStep.get(mPositionChange).text)
            }
        }catch (_:Exception) {

        }
    }

    private fun initializeViews(view: View) {
        loadingDialog = LoadingDialog(requireContext())
        mCloseBtn = view.findViewById(R.id.add_new_post_step2_close_btn)
        mBackBtn = view.findViewById(R.id.add_new_post_step2_back_btn)
        mTitleInput = view.findViewById(R.id.add_new_post_step2_title_input_edit_text)
        mMessageInput = view.findViewById(R.id.add_new_post_step2_body_input_edit_text)
        mImageButton = view.findViewById(R.id.add_new_post_step2_camera_button)
        mImagePost = view.findViewById(R.id.add_new_post_step2_image_view)
        mUpdateButton = view.findViewById(R.id.add_new_post_step2_update_button)

        mUpdateButton.setOnClickListener {
            updateGoal()
        }

        mImageButton.setOnClickListener {
            imagePickerHelper.openGallery()
        }

        mCloseBtn.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_CANCELED)
            requireActivity().finish()
        }

        mBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addNewPostStep2Fragment_to_addNewPostStep1Fragment)
        }

    }

    private fun updateGoal() {
        val postTitle = mTitleInput.text?.toString()?.trim() ?: ""
        val newMessage = mMessageInput.text?.toString()?.trim() ?: ""
        val bitmap = mImagePost.drawable.toBitmap()
        val postImageString = ImageUtils.convertBitmapToBase64(bitmap)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        var newPost = Post(userId = userId ,title = postTitle, message = newMessage, image = postImageString)
        mGoal.posts.add(newPost)
        viewModel.uploadPost(mGoal, mGoal.posts.lastIndex)
    }

    private fun setupObservers() {
        viewModel.addNewPostStep2State.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddNewPostStep2State.Loading -> loadingDialog.show()
                is AddNewPostStep2State.UpdatePostSuccess -> {
                    loadingDialog.dismiss()
                    val customPopup = SuccessDialog(requireActivity())
                    customPopup.show(
                        "Success",
                        "Your post has been sent to followers",
                        "Close",
                        onButtonClick = {
                            requireActivity().setResult(android.app.Activity.RESULT_OK)
                            requireActivity().finish()
                        }
                    )

                }
                is AddNewPostStep2State.UpdatePostError -> {
                    loadingDialog.dismiss()
                    val errorDialog = ErrorDialog(requireActivity())
                    errorDialog.show(
                        "Oops",
                        state.message,
                        "close"
                    )
                }

                else -> {}
            }
        }
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

}