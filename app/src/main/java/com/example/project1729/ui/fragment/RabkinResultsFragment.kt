package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS.CURRENT_MEASURE
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_MAX_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_USED_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.RABKIN_WRONG_ANSWERS
import com.example.project1729.data.keys.SIVTSEV_RESULTS.SIVTSEV_RESULT
import com.example.project1729.databinding.FragmentRabkinResultsBinding
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RabkinResultsFragment : Fragment() {

    private lateinit var binding: FragmentRabkinResultsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<RabkinTestViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRabkinResultsBinding.inflate(inflater, container, false)

        initializeFragment()

        binding.rabkinResultsFinishButton.setOnClickListener {

            val navOptions = NavOptions.Builder()
                .setPopUpTo(findNavController().graph.startDestinationId, true) // Очищает весь back stack
                .build()
            findNavController().navigate(R.id.action_rabkinResultsFragment_to_menuFragment, null, navOptions)

            RABKIN_RIGHT_ANSWERS = 0
            RABKIN_WRONG_ANSWERS = 0
            RABKIN_DEITERANOPY_ANSWERS = 0
            RABKIN_PROTANOPY_ANSWERS = 0
            RABKIN_USED_TESTS = mutableListOf()
        }

        binding.rabkinResultsResults.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.rabkinResultOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        return binding.root
    }

    private fun initializeFragment() {

        val resultPicture: Int
        binding.rabkinResultsFinishButton.isEnabled = false
        if (CURRENT_MEASURE == "rabkin") {
            binding.rabkinResultsText.setText(R.string.rabkin_results_text)
            if (RABKIN_RIGHT_ANSWERS >= RABKIN_MAX_RIGHT_ANSWERS) {
                resultPicture = R.drawable.picture_normal_colors
                binding.rabkinResultsResultText.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_okay)

            } else if (RABKIN_PROTANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS) {
                resultPicture = R.drawable.picture_protanopy
                binding.rabkinResultsResultText.setText(R.string.rabkin_protanopy)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_protanopy)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_protanopy)
            } else if (RABKIN_DEITERANOPY_ANSWERS == RABKIN_MAX_WRONG_ANSWERS) {
                resultPicture = R.drawable.picture_deiteranopy
                binding.rabkinResultsResultText.setText(R.string.rabkin_deiteranopy)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_deiteranopy)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_deiteranopy)
            } else {
                resultPicture = R.drawable.picture_dihromazy
                binding.rabkinResultsResultText.setText(R.string.rabkin_dihromasuim)
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_dihromasuim)
                binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_dihromazy)

            }

            Glide.with(binding.rabkinResultsResultImage)
                .load(resultPicture)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.picture_normal_colors)
                .into(binding.rabkinResultsResultImage)

        } else {
            binding.rabkinResultsText.setText(R.string.sivtsev_results_text)
            if (SIVTSEV_RESULT == "OK") {
                binding.rabkinResultsResultText.setText(R.string.rabkin_okay)
                resultPicture = R.drawable.sivtsev_result_ok
                binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_okay)
                binding.rabkinResultBottomSheetText.setText(R.string.sivtsev_results_guide_okay)
            } else {
                binding.rabkinResultsResultText.setText(R.string.sivtsev_bad)
                resultPicture = R.drawable.sivtsev_result_bad
                binding.rabkinResultBottomSheetTitle.setText(R.string.sivtsev_bad)
                binding.rabkinResultBottomSheetText.setText(R.string.sivtsev_results_guide_bad)
            }
            Glide.with(binding.rabkinResultsResultImage)
                .load(resultPicture)
                .transform(RoundedCorners(20))
                .placeholder(R.drawable.sivtsev_result_ok)
                .into(binding.rabkinResultsResultImage)

        }

        SIVTSEV_RESULT = "-"
        CURRENT_MEASURE = "-"



        bottomSheetBehavior = BottomSheetBehavior.from(binding.rabkinResultBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :

            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.rabkinResultOverlay.visibility = View.GONE
                    }

                    else -> {
                        binding.rabkinResultOverlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })


        lifecycleScope.launch {
            binding.rabkinResultsFinishButton.background =
                requireActivity().getDrawable(R.drawable.btn_start)
            binding.rabkinResultsFinishButtonText.setTextColor(requireActivity().getColor(R.color.white))
            delay(1000L)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.rabkinResultsFinishButton.isEnabled = true
            binding.rabkinResultsFinishButton.visibility = View.VISIBLE
            binding.rabkinResultsFinishButtonText.visibility = View.VISIBLE


            delay(10000L)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

        }
    }


}