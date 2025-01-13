package com.example.project1729.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project1729.databinding.FragmentResultsListBinding
import com.example.project1729.domain.model.Check
import com.example.project1729.ui.adapters.ResultsListAdapter
import com.example.project1729.ui.view_model.ResultsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResultsListFragment : Fragment() {

    private lateinit var binding: FragmentResultsListBinding
    private val viewModel by viewModel<ResultsViewModel>()
    private var currentChecksList = mutableListOf<Check>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getState().observe(viewLifecycleOwner){checksList ->
            render(checksList)

        }

        binding.resultsListList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, true)
    }

    private fun render(checksList: List<Check>){
        if (currentChecksList != checksList){
            currentChecksList = checksList.toMutableList()
            binding.resultsListList.adapter = ResultsListAdapter(currentChecksList)
        }

        if (currentChecksList.isEmpty()){
            binding.resultsListList.visibility = View.GONE
            binding.resultsListClearButton.visibility = View.GONE
            binding.resultsListPlaceholder.visibility = View.VISIBLE
        }

        else{
            binding.resultsListList.visibility = View.VISIBLE
            binding.resultsListClearButton.visibility = View.VISIBLE
            binding.resultsListPlaceholder.visibility = View.GONE
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.getChecks()
    }
}