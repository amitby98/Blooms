package com.example.blooms.mainApp.allMyGoal

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.addNewPost.AddNewPostActivity
import com.example.blooms.allMyPosts.AllMyPostsActivity
import com.example.blooms.general.Constance.ADD_NEW_POST_FROM_GOAL
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.ImageUtils
import com.example.blooms.general.LoadingDialog
import com.example.blooms.general.showCustomToast
import com.example.blooms.mainApp.allMyGoal.adapter.AllMyGoalsAdapter
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalState
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalViewModel
import com.example.blooms.model.Goal
import com.example.blooms.model.User
import com.google.android.material.button.MaterialButton


class AllMyGoalFragment : Fragment() {
    private val viewModel: AllMyGoalViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var mHelloName: AppCompatTextView
    private lateinit var mImageProfile: AppCompatImageView
    private lateinit var mAllMyPosts: MaterialButton

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadingDialog.show()
            viewModel.getAllMyGoals()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_my_goals, container, false)
        viewModel.getUserData()
        initializeViews(view)
        setupObservers()
        return view
    }

    private fun initializeViews(view : View) {
        loadingDialog = LoadingDialog(requireContext())
        recyclerView = view.findViewById(R.id.recyclerView)
        mHelloName = view.findViewById(R.id.my_goal_hello_text)
        mImageProfile = view.findViewById(R.id.my_goal_profile_image)
        mAllMyPosts = view.findViewById(R.id.my_goal_all_post_button)

        mAllMyPosts.setOnClickListener {
            getAllMyPost()
        }
    }


    private fun setupObservers() {
        viewModel.allMyGoalState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllMyGoalState.Loading -> loadingDialog.show()
                is AllMyGoalState.GetUserDataSuccess -> populateUserData(state.user)
                is AllMyGoalState.GetAllMyGoalsSuccess -> {
                    populateData(state.goals)
                    loadingDialog.dismiss()
                }
                is AllMyGoalState.GetAllMyGoalsError -> {
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

    private fun populateUserData(user: User) {
        mHelloName.text = "Hello, ${toTitleCase(user.firstName)}!"
        mImageProfile.setImageBitmap(ImageUtils.convertBase64ToBitmap(user.profileImage))
    }

    private fun toTitleCase(text: String): String {
        if (text.isEmpty()) return text

        return text.split(" ").joinToString(" ") { word ->
            if (word.isEmpty()) word
            else word.substring(0, 1).uppercase() + word.substring(1).lowercase()
        }
    }

    private fun populateData(goals: List<Goal>) {
        val adapter = AllMyGoalsAdapter(requireActivity(), goals) { goal ->
        //getAllMyPost()
        addNewPost(goal)
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns
        recyclerView.adapter = adapter
    }

    private fun addNewPost(goal: Goal) {
        val intent = Intent(requireContext(), AddNewPostActivity::class.java)
        intent.putExtra(ADD_NEW_POST_FROM_GOAL, goal)
        launcher.launch(intent)
    }

    private fun getAllMyPost() {
        val intent = Intent(requireContext(), AllMyPostsActivity::class.java)
        launcher.launch(intent)
    }
}