package com.example.blooms.mainApp.addTarget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blooms.R



class AddNewTargetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_target, container, false)

        return view
    }


    companion object {
        fun newInstance() = AddNewTargetFragment()
    }

}