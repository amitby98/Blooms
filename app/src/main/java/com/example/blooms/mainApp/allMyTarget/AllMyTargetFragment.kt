package com.example.blooms.mainApp.allMyTarget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooms.R
import com.example.blooms.mainApp.allMyTarget.allMyTargetViewModel.AllMyTargetViewModel


class AllMyTargetFragment : Fragment() {
    private val viewModel: AllMyTargetViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_my_target, container, false)
        viewModel.getAllMyTarget()
        return view
    }


    companion object {
        fun newInstance() = AllMyTargetFragment()
    }

}