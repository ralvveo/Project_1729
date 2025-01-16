package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.databinding.FragmentRabkinGuideBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RabkinGuideFragment : Fragment() {

    private lateinit var binding: FragmentRabkinGuideBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRabkinGuideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rabkinStartButton.setOnClickListener {
            lifecycleScope.launch {
                delay(200L)
                findNavController().navigate(R.id.action_rabkinGuideFragment_to_rabkinTestFragment)
                SHOW_QUESTION = true
            }
        }
    }
}