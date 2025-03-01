package com.example.blooms.addNewPost.step2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.fragment.findNavController
import com.example.blooms.R
import com.google.android.material.button.MaterialButton


class AddNewPostStep2Fragment : Fragment() {

    private lateinit var mCloseBtn: AppCompatImageView
    private lateinit var mBackBtn: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_post_step2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews(view)
    }

    private fun initializeViews(view: View) {
        mCloseBtn = view.findViewById(R.id.add_new_post_step2_close_btn)
        mBackBtn = view.findViewById(R.id.add_new_post_step2_back_btn)

        mCloseBtn.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_CANCELED)
            requireActivity().finish()
        }

        mBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_addNewPostStep2Fragment_to_addNewPostStep1Fragment)
        }

    }

}