package com.example.blooms.allMyPosts.Lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.addNewPost.step1.AddNewPostStep1FragmentDirections
import com.example.blooms.allMyPosts.Lobby.AllMyPostsLobbyViewModel.AllMyPostsLobbyState
import com.example.blooms.allMyPosts.Lobby.AllMyPostsLobbyViewModel.AllMyPostsLobbyViewModel
import com.example.blooms.allMyPosts.Lobby.adapter.AllMyPostsLobbyAdapter
import com.example.blooms.general.ErrorDialog
import com.example.blooms.general.LoadingDialog
import com.example.blooms.model.Goal
import com.example.blooms.model.Post

class AllMyPostsLobbyFragment : Fragment() {
    private lateinit var mCloseBtn: AppCompatImageView
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: AllMyPostsLobbyAdapter
    private lateinit var allMyPosts: ArrayList<Post>
    private lateinit var loadingDialog: LoadingDialog
    private val viewModel: AllMyPostsLobbyViewModel by viewModels()
    private var allGoals: List<Goal>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_my_posts_lobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        initializeViews(view)

    }


    private fun initializeViews(view: View) {
        mCloseBtn = view.findViewById(R.id.all_my_posts_lobby_close_btn)
        mRecyclerView = view.findViewById(R.id.all_my_posts_lobby_recyclerview)
        loadingDialog = LoadingDialog(requireContext())
        viewModel.getUserData()

        mCloseBtn.setOnClickListener {
            requireActivity().setResult(android.app.Activity.RESULT_CANCELED)
            requireActivity().finish()
        }
    }

    private fun setupObservers() {
        viewModel.allMyPostsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AllMyPostsLobbyState.Loading -> loadingDialog.show()
                is AllMyPostsLobbyState.GetAllMyPostsSuccess -> {
                    loadingDialog.dismiss()
                    allGoals = state.goals
                    var editPagePostWithGoalIDList = viewModel.getAllRelevantPosts(state.goals)
                    adapter = AllMyPostsLobbyAdapter(editPagePostWithGoalIDList){ position ->
                        var postId = editPagePostWithGoalIDList.get(position).post.postId
                        var goalId = editPagePostWithGoalIDList.get(position).goalId
                        var specificGoal = allGoals?.find { it.goalId == goalId }
                        val action = AllMyPostsLobbyFragmentDirections.actionAllMyPostsLobbyFragmentToAllMyPostsEditFragment(
                            specificGoal!!, postId)
                        findNavController().navigate(action)
                    }
                    mRecyclerView.layoutManager = LinearLayoutManager(context)
                    mRecyclerView.adapter = adapter
                }
                is AllMyPostsLobbyState.GetAllMyPostsError -> {
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