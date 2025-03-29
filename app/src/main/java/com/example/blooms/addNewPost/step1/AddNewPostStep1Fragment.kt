package com.example.blooms.addNewPost.step1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.addNewPost.step1.adapter.NewPostStep1GoalStepAdapter
import com.example.blooms.addNewPost.step2.AddNewPostStep2FragmentDirections
import com.example.blooms.auth.authFragments.RegisterFragmentDirections
import com.example.blooms.mainApp.addNewGoal.goalStep.GoalStepAdapter
import com.example.blooms.model.Goal
import com.google.android.material.button.MaterialButton

class AddNewPostStep1Fragment : Fragment() {

    private lateinit var mCloseBtn: AppCompatImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: NewPostStep1GoalStepAdapter
    private lateinit var mGoal: Goal

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_post_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)

        try {
            // Retrieve the Goal object passed from the Activity
            if (arguments != null) {
                val args = AddNewPostStep1FragmentArgs.fromBundle(requireArguments())
                mGoal = args.goal  // This is the Goal object passed via Safe Args
                adapter = NewPostStep1GoalStepAdapter(requireContext(), mGoal.goalStep){ item , position->
                    mGoal.goalStep[position].isChecked = true
                    val action = AddNewPostStep1FragmentDirections.actionAddNewPostStep1FragmentToAddNewPostStep2Fragment(mGoal, position)
                    findNavController().navigate(action)
                }
                mRecyclerView.layoutManager = LinearLayoutManager(context)
                mRecyclerView.adapter = adapter
            }
        }catch (e:Exception) {

        }
    }


    private fun initializeViews(view: View) {
        mCloseBtn = view.findViewById(R.id.add_new_post_step1_close_btn)
        mRecyclerView = view.findViewById(R.id.add_new_post_step1_target_recyclerview)

        mCloseBtn.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_CANCELED)
            requireActivity().finish()
        }
    }}