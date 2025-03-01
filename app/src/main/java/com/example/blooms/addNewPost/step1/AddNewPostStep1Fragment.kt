package com.example.blooms.addNewPost.step1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.blooms.R
import com.google.android.material.button.MaterialButton

class AddNewPostStep1Fragment : Fragment() {

    private lateinit var mCloseBtn: AppCompatImageView
    private lateinit var mContinueBtn: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_post_step1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
    }


    private fun initializeViews(view: View) {
        mCloseBtn = view.findViewById(R.id.add_new_post_step1_close_btn)
        mContinueBtn = view.findViewById(R.id.add_new_post_step1_continue_btn)


        mCloseBtn.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_CANCELED)
            requireActivity().finish()
        }

        mContinueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addNewPostStep1Fragment_to_addNewPostStep2Fragment)
        }
    }}