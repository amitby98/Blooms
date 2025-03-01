package com.example.blooms.mainApp.addNewGoal.goalStep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blooms.R
import com.example.blooms.general.showCustomToast
import com.example.blooms.model.GoalStep

class GoalsDialog(private val itemList : ArrayList<GoalStep>, private val onListPicked: (ArrayList<GoalStep>) -> Unit) : DialogFragment() {

    private lateinit var adapter: GoalStepAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.goal_dialog, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.goal_step_add_step_line_recyclerview)
        val editText : AppCompatEditText = view.findViewById(R.id.goal_step_add_step_edit_text)
        val addButton : AppCompatImageView = view.findViewById(R.id.goal_step_add_step_add_button)
        val okButton : AppCompatButton = view.findViewById(R.id.add_new_post_step1_continue_btn)

        adapter = GoalStepAdapter(itemList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            if(itemList.size == 5) {
                showCustomToast("This is the max goals")
            } else {
                val text = editText.text.toString()
                if (text.isNotEmpty()) {
                    adapter.addItem(text)
                    editText.text?.clear()
                }
            }
        }

        okButton.setOnClickListener {
            onListPicked(itemList)
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // Optional: Transparent background
    }
}
