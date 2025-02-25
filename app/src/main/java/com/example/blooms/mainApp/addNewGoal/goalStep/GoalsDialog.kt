package com.example.blooms.mainApp.addNewGoal.goalStep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
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

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val editText : AppCompatEditText = view.findViewById(R.id.editText)
        val addButton : AppCompatButton = view.findViewById(R.id.addButton)
        val okButton : AppCompatButton = view.findViewById(R.id.okButton)

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
