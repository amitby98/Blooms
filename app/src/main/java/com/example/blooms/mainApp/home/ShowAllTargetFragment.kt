package com.example.blooms.mainApp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blooms.R



class ShowAllTargetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_target, container, false)

        return view
    }


    companion object {
        fun newInstance() = ShowAllTargetFragment()
    }

}