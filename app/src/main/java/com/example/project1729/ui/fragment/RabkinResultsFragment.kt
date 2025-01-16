package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.project1729.R
import com.example.project1729.data.keys.RABKIN_RESULTS
import com.example.project1729.data.keys.RABKIN_RESULTS.DEITERANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.MAX_RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.MAX_WRONG_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.PROTANOPY_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.RIGHT_ANSWERS
import com.example.project1729.data.keys.RABKIN_RESULTS.USED_TESTS
import com.example.project1729.data.keys.RABKIN_RESULTS.WRONG_ANSWERS
import com.example.project1729.databinding.FragmentRabkinResultsBinding
import com.example.project1729.ui.view_model.RabkinTestViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class RabkinResultsFragment : Fragment() {

    private lateinit var binding: FragmentRabkinResultsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel by viewModel<RabkinTestViewModel>()


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRabkinResultsBinding.inflate(inflater, container, false)

        initializeFragment()

        binding.rabkinResultsFinishButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
            backPressedCallback.isEnabled = true
            fragmentManager?.popBackStack()
            findNavController().navigate(R.id.action_rabkinResultsFragment_to_menuFragment)

            RIGHT_ANSWERS = 0
            WRONG_ANSWERS = 0
            DEITERANOPY_ANSWERS = 0
            PROTANOPY_ANSWERS = 0
            USED_TESTS = mutableListOf()
        }

        binding.rabkinResultsResults.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.rabkinResultOverlay.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        return binding.root
    }

    private fun initializeFragment(){

        val rabkinResultPicture: Int
        if (RIGHT_ANSWERS == MAX_RIGHT_ANSWERS){
            rabkinResultPicture = R.drawable.picture_normal_colors
            binding.rabkinResultsResultText.setText(R.string.rabkin_okay)
            binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_okay)
            binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_okay)

        }



        else if (PROTANOPY_ANSWERS == MAX_WRONG_ANSWERS){
            rabkinResultPicture = R.drawable.picture_protanopy
            binding.rabkinResultsResultText.setText(R.string.rabkin_protanopy)
            binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_protanopy)
            binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_protanopy)
        }

        else if (DEITERANOPY_ANSWERS == MAX_WRONG_ANSWERS){
            rabkinResultPicture = R.drawable.picture_deiteranopy
            binding.rabkinResultsResultText.setText(R.string.rabkin_deiteranopy)
            binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_deiteranopy)
            binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_deiteranopy)
        }

        else {
            rabkinResultPicture = R.drawable.picture_dihromazy
            binding.rabkinResultsResultText.setText(R.string.rabkin_dihromasuim)
            binding.rabkinResultBottomSheetTitle.setText(R.string.rabkin_dihromasuim)
            binding.rabkinResultBottomSheetText.setText(R.string.rabkin_results_guide_dihromazy)

        }

        Glide.with(binding.rabkinResultsResultImage)
            .load(rabkinResultPicture)
            .transform(RoundedCorners(20))
            .placeholder(R.drawable.picture_normal_colors)
            .into(binding.rabkinResultsResultImage)



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
    }


}