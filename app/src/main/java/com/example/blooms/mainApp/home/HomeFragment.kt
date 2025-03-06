package com.example.blooms.mainApp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.LoadingDialog
import com.example.blooms.mainApp.addNewGoal.adapter.CategoryAdapter
import com.example.blooms.mainApp.allMyGoal.adapter.AllMyGoalsAdapter
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalState
import com.example.blooms.mainApp.home.adapter.AllPostsAdapter
import com.example.blooms.mainApp.home.homeViewModel.HomeState
import com.example.blooms.mainApp.home.homeViewModel.HomeViewModel
import com.example.blooms.model.HomePagePosts


class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllPostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        initializeViews(view)
        setupObservers()
        viewModel.getAllPosts()
        return view
    }

    private fun initializeViews(view : View) {
        loadingDialog = LoadingDialog(requireContext())
        recyclerView = view.findViewById(R.id.homepage_recycler)
    }

    private fun initData(list: List<HomePagePosts>) {
        val adapter = AllPostsAdapter(list) { post ->

        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.homeState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> loadingDialog.show()
                is HomeState.GetAllPostsSuccess -> {
                    loadingDialog.dismiss()
                    initData(state.posts)
                }
                is HomeState.GetAllGoalsError -> {
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


}