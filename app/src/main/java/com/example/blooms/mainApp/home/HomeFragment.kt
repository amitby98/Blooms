package com.example.blooms.mainApp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooms.R
import com.example.blooms.mainApp.home.homeViewModel.HomeViewModel


class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel.getAllPosts()
        return view
    }


    companion object {
        fun newInstance() = HomeFragment()
    }

}