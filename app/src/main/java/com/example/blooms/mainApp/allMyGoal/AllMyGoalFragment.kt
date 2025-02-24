package com.example.blooms.mainApp.allMyGoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.blooms.R
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.LoadingDialog
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalState
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalViewModel


class AllMyGoalFragment : Fragment() {
    private val viewModel: AllMyGoalViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_my_goals, container, false)
        viewModel.getAllMyGoals()
        initializeViews(view)
        setupObservers()
        return view
    }

    private fun initializeViews(view : View) {
        loadingDialog = LoadingDialog(requireContext())
    }


    private fun setupObservers() {
        viewModel.allMyGoalState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllMyGoalState.Loading -> loadingDialog.show()
                is AllMyGoalState.GetAllMyPostSuccess -> {
                    loadingDialog.dismiss()
                }
                is AllMyGoalState.GetAllMyPostError -> {
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

    companion object {
        fun newInstance() = AllMyGoalFragment()
    }

}