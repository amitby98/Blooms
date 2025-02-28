package com.example.blooms.mainApp.allMyGoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.LoadingDialog
import com.example.blooms.general.showCustomToast
import com.example.blooms.mainApp.allMyGoal.adapter.AllMyGoalsAdapter
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalState
import com.example.blooms.mainApp.allMyGoal.allMyGoalViewModel.AllMyGoalViewModel
import com.example.blooms.model.Goal


class AllMyGoalFragment : Fragment() {
    private val viewModel: AllMyGoalViewModel by viewModels()
    private lateinit var loadingDialog: LoadingDialog

    private val goals = arrayOf(
        Goal(), Goal(), Goal(),
        Goal(), Goal(), Goal()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_all_my_goals, container, false)
        viewModel.getAllMyGoals()
        initializeViews(view)
        setupObservers()

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val adapter = AllMyGoalsAdapter(requireActivity(), goals) { position ->
            showCustomToast( "Clicked Item: $position")
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 2 columns

        recyclerView.adapter = adapter


        return view
    }

    private fun initializeViews(view : View) {
        loadingDialog = LoadingDialog(requireContext())
    }


    private fun setupObservers() {
        viewModel.allMyGoalState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllMyGoalState.Loading -> loadingDialog.show()
                is AllMyGoalState.GetAllMyGoalsSuccess -> {
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

    companion object {
        fun newInstance() = AllMyGoalFragment()
    }

}