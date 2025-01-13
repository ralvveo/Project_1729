package com.example.project1729.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.project1729.databinding.FragmentResultsGraphBinding
import com.example.project1729.domain.model.Check
import com.example.project1729.ui.view_model.ResultsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultsGraphFragment : Fragment() {

    private var currentChecksList = mutableListOf<Check>()
    private lateinit var binding: FragmentResultsGraphBinding
    private val viewModel by viewModel<ResultsViewModel>()
    private var leftBarSet = mutableListOf<Pair<String, Float>>()
    private var rightBarSet = mutableListOf<Pair<String, Float>>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsGraphBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner){checksList ->
            render(checksList)

        }

        binding.barChartLeft.animation.duration = ANIMATION_DURATION
        binding.barChartRight.animation.duration = ANIMATION_DURATION



    }

    private fun render(checksList: List<Check>){
        if (currentChecksList != checksList){
            currentChecksList = checksList.toMutableList()

        }
        leftBarSet = mutableListOf()
        rightBarSet = mutableListOf()
        if (currentChecksList.isEmpty()){
            binding.resultsGraphPlaceholder.visibility = View.VISIBLE
            binding.barChartLeft.visibility = View.GONE
            binding.barLeftTitle.visibility = View.GONE
            binding.barRightTitle.visibility = View.GONE
        }
        else{
            for (check in checksList){
                if (check.eye == "Левый")
                    leftBarSet.add(check.dateAndTime to check.KCHSM.toFloat())
                else{
                    rightBarSet.add(check.dateAndTime to check.KCHSM.toFloat())
                }
            }
            binding.barChartLeft.animate(leftBarSet)
            binding.barChartRight.animate(rightBarSet)


        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChecks()
    }

    companion object{
        private const val ANIMATION_DURATION = 2000L
    }
}