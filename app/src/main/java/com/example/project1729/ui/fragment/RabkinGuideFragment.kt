package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.SHOW_QUESTION
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_CURRENT_LEVEL_WRONG
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
        if (CURRENT_MEASURE == "rabkin"){
            binding.rabkinGuideTitle.setText(R.string.rabkin_guide_title)
            binding.rabkinGuideText.setText(R.string.rabkin_guide_text)
            binding.rabkinGuideGiude.setText(R.string.rabkin_guide_guide)
            binding.rabkinGuideImage.setBackgroundResource(R.drawable.rabkin_preview_pic)

            RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_WRONG_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS = 0
            RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS = 0
        }
        else{
            binding.rabkinGuideTitle.setText(R.string.sivtsev_guide_title)
            binding.rabkinGuideText.setText(R.string.sivtsev_guide_text)
            binding.rabkinGuideGiude.setText(R.string.sivtsev_guide_guide)
            binding.rabkinGuideImage.setBackgroundResource(R.drawable.sivtsev_preview_pic)

            SIVTSEV_CURRENT_LEVEL = 1
            SIVTSEV_CURRENT_LEVEL_WRONG = 0
        }

        binding.rabkinGuideButtonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.rabkinStartButton.isEnabled = false

        lifecycleScope.launch {

            val startTestingString = getString(R.string.start_testing)
            binding.startButtonLoginText.text = "$startTestingString (6)"
            delay(500L)
            binding.startButtonLoginText.text = "$startTestingString (5)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (4)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (3)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (2)"
            delay(1000L)
            binding.startButtonLoginText.text = "$startTestingString (1)"
            delay(1000L)
            binding.startButtonLoginText.text = startTestingString
            binding.rabkinGuideGiude.setTextColor(requireActivity().getColor(R.color.black))

            binding.rabkinStartButton.isEnabled = true
            binding.rabkinStartButton.background = requireActivity().getDrawable(R.drawable.btn_start)
            binding.startButtonLoginText.setTextColor(requireActivity().getColor(R.color.white))
            binding.rabkinGuideLoader.visibility = View.GONE
        }


    }
}