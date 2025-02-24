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

class GoalsDialog(private val itemList : ArrayList<String>, private val onListPicked: (ArrayList<String>) -> Unit) : DialogFragment() {

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
            val text = editText.text.toString()
            if (text.isNotEmpty()) {
                adapter.addItem(text)
                editText.text?.clear()
            }
        }

        okButton.setOnClickListener {
            onListPicked(itemList)
            dismiss()
        }

        return view
    }
}
