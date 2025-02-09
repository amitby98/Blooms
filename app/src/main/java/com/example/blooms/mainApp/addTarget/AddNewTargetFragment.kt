package com.example.blooms.mainApp.addTarget

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.mainApp.addTarget.adapter.CategoryAdapter
import com.example.blooms.model.Category


class AddNewTargetFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private var categories = mutableListOf<Category>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_target, container, false)

        initializeViews(view)
        setCategoryList()
        return view
    }

    private fun initializeViews(view: View) {
        recyclerView = view.findViewById(R.id.target_recycler_category)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun  setCategoryList() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        categories = mutableListOf(
            Category("Fitness", R.drawable.fitness_running_icon),
            Category("Economy", R.drawable.economic),
            Category("Family", R.drawable.family_care_father_mother_icon),
            Category("Shopping", R.drawable.shopping_bag),
            Category("Vacation", R.drawable.vacation),
            Category("Health", R.drawable.health_healthcare_medical_icon),
            Category("Other", R.drawable.other_icon)
        )

        adapter = CategoryAdapter(categories) { selectedPosition ->
            categories.forEachIndexed { index, category -> category.isSelected = index == selectedPosition }
            adapter.notifyDataSetChanged()
        }

        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = AddNewTargetFragment()
    }

}