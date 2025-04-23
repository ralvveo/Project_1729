package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.databinding.FragmentCheckBinding
import com.example.project1729.ui.view_model.CheckViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckFragment : Fragment() {

    private lateinit var binding: FragmentCheckBinding
    private val viewModel by viewModel <CheckViewModel> ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkButtonStartCheck.setOnClickListener {
            findNavController().navigate(R.id.action_checkFragment_to_dopInfoFragment)
        }
    }
}